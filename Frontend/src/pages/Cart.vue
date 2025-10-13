<script setup>
import BaseButton from '@/components/elements/BaseButton.vue';
import { ref, computed, watch } from 'vue';
import { useUserStore } from '@/stores/UserStore';
import { storeToRefs } from 'pinia';
import { postData, patchItem, deleteItemById } from '@/libs/fetchUtils'
import PopupMessage from '../components/elements/PopupMessage.vue'
import FormInput from '@/components/elements/FormInput.vue';
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue';
import emptySaleItemsImg from '@/assets/images/emptySaleItems.png'
import router from '@/router';

const userStore = useUserStore()
const { removeFromCart, getAccessToken, getUserId, updateCartItemQty, setCart } = userStore
const { cart } = storeToRefs(userStore)

const selectAll = ref(false)
const sellerChecks = ref([])
const itemChecks = ref([])

const isShowPopUp = ref(false)
const popupMessage = ref('')
const isSuccess = ref(false)

const updateSelectAll = () => {
    selectAll.value = sellerChecks.value.every(checked => checked)
}

watch(cart, (newCart) => {
    sellerChecks.value = newCart.map(() => false)
    itemChecks.value = newCart.map(seller =>
        seller.cartItems.map(() => false)
    ) 
}, {immediate: true})

const toggleSelectAll = () => {
  sellerChecks.value = cart.value.map(() => selectAll.value)
  itemChecks.value = cart.value.map(seller => seller.cartItems.map(() => selectAll.value))
}

const toggleSeller = (sellerIndex) => {
  itemChecks.value[sellerIndex] = itemChecks.value[sellerIndex].map(() => sellerChecks.value[sellerIndex])
  updateSelectAll()
}

const toggleItem = (sellerIndex) => {
  sellerChecks.value[sellerIndex] = itemChecks.value[sellerIndex].every(checked => checked)
  updateSelectAll()
}

const totalItems = computed(() => {
  let count = 0
  cart.value.forEach((seller, si) => {
    seller.cartItems.forEach((item, ii) => {
      if (itemChecks.value[si]?.[ii]) {
        count += item.quantity
      }
    })
  })
  return count
})

const totalPrice = computed(() => {
  let sum = 0
  cart.value.forEach((seller, si) => {
    seller.cartItems.forEach((item, ii) => {
      if (itemChecks.value[si]?.[ii]) {
        sum += item.priceEach * item.quantity
      }
    })
  })
  return sum
})

const placeOrder = async () => {
    isSuccess.value = false
    isShowPopUp.value = false
    const orders = cart.value.map((c, si) => {
        const selectedItems = c.cartItems.filter((_, ii) => itemChecks.value[si]?.[ii])
        if (selectedItems.length === 0) return null
        const orderDate = new Date().toISOString()
        return {
            buyerId : getUserId(),
            sellerId: c.seller.id,
            orderDate,
            shippingAddress: address.value,
            orderNote: note.value,
            orderItems: selectedItems.map(item => ({
                saleItemId: item.saleItemId,
                price: item.priceEach,
                quantity: item.quantity,
                description: `${item.brandName} ${item.model} (${item.storageGb}GB, ${item.color})`
            }))
        }
    }).filter(o => o !== null)

    try {
        const placeOrder = await postData(`${import.meta.env.VITE_APP_URL}/v2/orders`, orders, getAccessToken())
        if (placeOrder.length) {
            isSuccess.value = true
            isShowPopUp.value = true
            popupMessage.value = 'Your order has been successfully processed.'
            setCart(cart.value.map((seller, si) => {
                const checks = itemChecks.value[si]
                if (checks.every(v => v === true)) {
                    return null
                }
                const remainingItems = seller.cartItems.filter((_, ii) => !checks[ii])
                return { ...seller, cartItems: remainingItems }
            }).filter(o => o !== null))
        } else if (placeOrder.status === 404){
            isShowPopUp.value = true
            popupMessage.value = 'Seller cannot buy their own products'
        } else if (placeOrder.status === 409){
            isShowPopUp.value = true
            const firstOrderWithItems = orders[0]
            const firstItemId = firstOrderWithItems?.orderItems[0]?.saleItemId
            popupMessage.value = 'Not enough stock for item ' + firstItemId
        } 
    } catch (error) {
        console.error(error)
    }
}

const updateQty = async (indexOfSeller, indexOfItem, cartItemId, newQty) => {
    try {
        console.log(newQty);
        
        const updatedItem = await patchItem(`${import.meta.env.VITE_APP_URL}/v2/carts`, cartItemId, {quantity : newQty}, getAccessToken())
        updateCartItemQty(indexOfSeller, indexOfItem, updatedItem.quantity)
    } catch (error) {
        console.log(error);
    }
}

const showDelConfirm = ref(false)
const deletedId = ref(null)
const deletedIndexOfSeller = ref(null)
const deletedIndexOfItem = ref(null)

function closeDelConfirm() {
    showDelConfirm.value = false
    deletedId.value = null
    deletedIndexOfSeller.value = null
    deletedIndexOfItem.value = null
}

async function deleteCartItem() {
    try {
        const status = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v2/carts`, deletedId.value, getAccessToken())
        if (status === 204) {
            removeFromCart(deletedIndexOfSeller.value, deletedIndexOfItem.value)
            showDelConfirm.value = false
            if (!cart.value.length) {
                router.push({ name : 'SaleItems'})
            }
        }
    } catch (error) {
        console.log(error);
    }
}

const decCartQty = async (indexOfSeller, indexOfItem, cartItemId) => {
    const quantity = cart.value[indexOfSeller].cartItems[indexOfItem].quantity
    if (quantity !== 1) {
        await updateQty(indexOfSeller, indexOfItem, cartItemId, quantity - 1)
    } else {
        showDelConfirm.value = true
        deletedId.value = cartItemId
        deletedIndexOfSeller.value = indexOfSeller
        deletedIndexOfItem.value = indexOfItem
    }
}

const incCartQty = async (indexOfSeller, indexOfItem, cartItemId) => {
    const maxQty = cart.value[indexOfSeller].cartItems[indexOfItem].maxQuantity
    const quantity = cart.value[indexOfSeller].cartItems[indexOfItem].quantity

    if (quantity < maxQty) {
        await updateQty(indexOfSeller, indexOfItem, cartItemId, quantity + 1)
    }
}   

const hasSelectedItems = computed(() => {
    return itemChecks.value.some(seller => seller.some(item => item))
})

const address = ref(null)
const note = ref(null)

address.value = localStorage.getItem('cartAddress') || ''
note.value = localStorage.getItem('cartNote') || ''

watch(address, (newValue) => {
  localStorage.setItem('cartAddress', newValue)
})

watch(note, (newValue) => {
  localStorage.setItem('cartNote', newValue)
})
</script>
 
<template>
    <div class="w-full min-h-screen font-rubik flex flex-col items-center text-[#332A1E] gap-10 bg-white pb-20 pt-25 px-10 md:pt-30 md:px-12 lg:pt-35 lg:px-25">
        <PopupMessage :message="popupMessage" :isShowPopup="isShowPopUp" :isSuccess="isSuccess" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25" />
            <p class="text-2xl sm:text-3xl lg:text-4xl font-bold">Shopping Cart</p>
            <div v-if="cart.length" class="flex flex-col xl:flex-row w-full gap-5 lg:gap-10 xl:gap-15">
                <div class="xl:w-2/3 h-fit space-y-3">
                    <div class="flex gap-3 border border-[#332A1E]/10 shadow-sm rounded-md p-3">
                        <label class="inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="selectAll" @change="toggleSelectAll" class="hidden peer itbms-select-all">
                            <div class="w-4 h-4 flex-shrink-0 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                                <svg v-if="selectAll" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                                    <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                                </svg>
                            </div>
                        </label>
                        <p class="font-medium text-sm xl:text-base">Select All</p>
                    </div>
                    <div v-for="(c, indexOfSeller) in cart" :key="c.seller.id" class="itbms-row border border-[#332A1E]/10 shadow-sm rounded-md px-3 pt-3">
                        <div class="flex gap-3 pb-3 border-b border-[#6F879C]/30">
                            <label class="inline-flex items-center cursor-pointer">
                                <input type="checkbox" v-model="sellerChecks[indexOfSeller]" @change="toggleSeller(indexOfSeller)" class="hidden peer itbms-select-nickname">
                                <div class="w-4 h-4 flex-shrink-0 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                                    <svg v-if="sellerChecks[indexOfSeller]" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                                        <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                                    </svg>
                                </div>
                            </label>
                            <div class="flex gap-2 items-center">
                                <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20">
                                    <path fill="#332A1E" d="M6.123 7.25L6.914 2H2.8L1.081 6.5q-.08.24-.081.5c0 1.104 1.15 2 2.571 2c1.31 0 2.393-.764 2.552-1.75M10 9c1.42 0 2.571-.896 2.571-2q-.001-.062-.005-.121L12.057 2H7.943l-.51 4.875L7.429 7c0 1.104 1.151 2 2.571 2m5 1.046V14H5v-3.948c-.438.158-.92.248-1.429.248c-.195 0-.384-.023-.571-.049V16.6c0 .77.629 1.4 1.398 1.4H15.6c.77 0 1.4-.631 1.4-1.4v-6.348a4 4 0 0 1-.571.049A4.2 4.2 0 0 1 15 10.046M18.92 6.5L17.199 2h-4.113l.79 5.242C14.03 8.232 15.113 9 16.429 9C17.849 9 19 8.104 19 7q-.001-.26-.08-.5" />
                                </svg>
                                <p class="itbms-nickname font-medium text-sm xl:text-base">{{ c.seller.userName }}</p>
                            </div>
                        </div>
                        <div v-for="(item, indexOfItem) in c.cartItems" :key="item.id" class="px-4 pt-4" >
                            <div class="itbms-item-row flex gap-4 items-center min-h-20 pb-4" :class="indexOfItem !== c.cartItems.length - 1 ? 'border-b border-[#332A1E]/10' : ''">
                                <label class="inline-flex items-center cursor-pointer">
                                    <input v-if="itemChecks[indexOfSeller]" type="checkbox" v-model="itemChecks[indexOfSeller][indexOfItem]" @change="toggleItem(indexOfSeller)"class="hidden peer"/>
                                    <div class="w-4 h-4 flex-shrink-0 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                                        <svg v-if="itemChecks[indexOfSeller]?.[indexOfItem]" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                                            <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                                        </svg>
                                    </div>
                                </label>
                                <div class="bg-[#FAF6F5] rounded-sm w-25 h-20 flex justify-center items-center">
                                    <img src="/saleItemImage/demoImg1.png" class="max-w-12 max-h-15"/>
                                </div>
                                <div class="w-full min-h-20 flex flex-col justify-between text-xs lg:text-sm xl:text-base gap-2">
                                    <p class="itbms-item-description break-words">
                                        <span class="font-medium">{{ `${item.brandName} ` }}</span>
                                        <span>{{ `${item.model} (${item.storageGb}GB, ${item.color})` }}</span>
                                    </p>
                                    <div class="flex justify-between flex-wrap gap-2">
                                        <p class="font-medium text-[#6F879C]">฿ <span>{{ (item.priceEach * item.quantity).toLocaleString() }}</span></p>
                                        <div class="flex gap-3 h-fit">
                                            <button @click="decCartQty(indexOfSeller, indexOfItem, item.id)" class="itbms-dec-qty-button px-3 rounded-sm bg-[#f5f0ec] hover:bg-[#ded8d2] cursor-pointer">-</button>
                                            <p class="itbms-item-quantity">{{ item.quantity }}</p>
                                            <button @click="incCartQty(indexOfSeller, indexOfItem, item.id)" class="itbms-inc-qty-button px-3 rounded-sm bg-[#f5f0ec] hover:bg-[#ded8d2] cursor-pointer">+</button>
                                        </div>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
                <div class="xl:w-1/3 border border-[#332A1E]/10 shadow-sm rounded-md p-4 space-y-5 h-fit">
                    <p class="text-lg sm:text-xl xl:text-2xl font-bold text-center">Cart Summary</p>
                    <hr class="text-[#332A1E]/10">
                    <div class="space-y-2 text-md sm:text-sm md:text-base xl:text-lg">
                        <p class="font-medium text-center">Shipping Address</p>
                        <FormInput v-model="address" label="Address" inputType="textarea" field="shippingAddress" placeholder="Shipping address" className="itbms-shipping-address" labelSize="sm:text-sm md:text-base xl:text-lg" inputSize="text-sm xl:text-base" :required="true" invalidMessage="Please enter your address."/>
                        <FormInput v-model="note" label="Note" inputType="textarea" field="orderNote" placeholder="Additional instructions or requests" className="itbms-order-note" labelSize="sm:text-sm md:text-base xl:text-lg" inputSize="text-sm xl:text-base"/>
                    </div>
                    <hr class="text-[#332A1E]/10">
                    <div class="text-md sm:text-sm md:text-base xl:text-lg space-y-1">
                        <div class="flex justify-between">
                            <p class="font-medium">Total items :</p>
                            <p class="itbms-total-order-items">{{ totalItems }}</p>
                        </div>
                        <div class="flex justify-between">
                            <p class="font-medium">Total price :</p>
                            <p class="flex gap-2">
                                <span>Bath</span>
                                <span class="itbms-total-order-price">{{ totalPrice.toLocaleString() }}</span>
                            </p>
                        </div>
                    </div>
                    <BaseButton text="Place order" bgColor="bg-[#6F879C] disabled:bg-[#ABBCC9]" textColor="text-white" class="itbms-place-order-button w-full disabled:border-[#ABBCC9]" @click="placeOrder" :disabled="!hasSelectedItems || !address"/>
                </div>
            </div>
            <div v-else class="flex flex-col items-center text-center gap-7 py-18">
                <img :src="emptySaleItemsImg" alt="EmptySaleItems" class=" w-36">
                <div class="space-y-3">
                    <p class="text-xl font-medium text-[#6F879C]">Your cart is empty</p>
                    <p class="text-[#ABBCC9]">
                        <span>Look like you haven't added</span>
                        <br>
                        <span>anything to your cart yet</span>
                    </p>
                </div>
                <router-link :to="{ name : 'SaleItems' }">
                    <BaseButton text="Shop Now"/>
                </router-link>
            </div>
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" :message="`Do you want to remove the sale item from the cart?`" class="itbms-message" @delete="deleteCartItem" />
    </div>
</template>
 
<style scoped>
</style>
