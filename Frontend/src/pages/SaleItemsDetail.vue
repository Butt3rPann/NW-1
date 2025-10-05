<script setup>
import { onMounted, ref } from 'vue'
import { useRoute } from 'vue-router'
import { deleteItemById, getItemById, postData } from '@/libs/fetchUtils.js'
import OptionsPhone from '@/components/sale-item/sale-item-detail/OptionsPhone.vue'
import ItemDetailRow from '@/components/sale-item/sale-item-detail/ItemDetailRow.vue'
import ErrorMessage from '@/components/elements/ErrorMessage.vue'
import { formatLocalTime } from '@/libs/datetimeUtils'
import backArrowIcon from '@/assets/images/backArrow.png'
import editIcon from "@/assets/images/edit.png"
import trashIcon from "@/assets/images/trash.png"
import router from '@/router'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import boxImg from '@/assets/images/box.png'
import productNotFound from '@/assets/images/product-not-found.png'
import cart from '@/assets/images/cart.png'
import { useUserStore } from '@/stores/UserStore'

const { params: { saleItemId } } = useRoute()
const userStore = useUserStore()
const { getUserId, addToCart, getAccessToken } = userStore

const selectedItem = ref({})

const phones = ref({
    mainImage: null,
    thumbnail: []
})

onMounted(async () => {
    try {
        selectedItem.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/sale-items`, saleItemId)
        console.log(selectedItem.value);
        
        selectedItem.value.createdOn = formatLocalTime(selectedItem.value.createdOn)
        selectedItem.value.updatedOn = formatLocalTime(selectedItem.value.updatedOn)

        if (selectedItem.value.saleItemImages && selectedItem.value.saleItemImages.length > 0) {
            phones.value.mainImage = `${import.meta.env.VITE_APP_URL}/v1/files/${selectedItem.value.saleItemImages[0].fileName}?t=${Date.now()}`  
            phones.value.thumbnail = selectedItem.value.saleItemImages.map(file => `${import.meta.env.VITE_APP_URL}/v1/files/${file.fileName}?t=${Date.now()}`)
        }
    } catch (error) {
        console.log(error)
    }
})

const selectedPhone = ref(0)

const changeMainImg = (index) => {
    selectedPhone.value = index
    phones.value.mainImage = phones.value.thumbnail[selectedPhone.value]
}

const route = useRoute()

const isShowPopup = ref(false)

if (route.query.edited === 'true') {
    router.replace({ query: { } })
    isShowPopup.value = true
}

const showNotFound = ref(false)

async function deleteSaleItem(){
    try {
        const status = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v2/sale-items`, saleItemId, getAccessToken())
        if (status === 404) showNotFound.value = true
        else router.push({name: 'SaleItems', query: {deleted: 'true'}})
    } catch (error) {
        console.log(error);  
    }
}

const showDelConfirm = ref(false)

function closeDelConfirm() {
    showDelConfirm.value = false
}

const cartQty = ref(1)

const incCartQty = () => {
    if (cartQty.value < selectedItem.value.quantity) {
        cartQty.value += 1
    }
}

const decCartQty = () => {
    if (cartQty.value !== 1) {
        cartQty.value -= 1
    }
}

const addItemToCart = async () => {
    try {
        const addedItem = await postData(
            `${import.meta.env.VITE_APP_URL}/v2/carts`, 
            {
                userId: getUserId(),
                saleItemId: selectedItem.value.id,
                model: selectedItem.value.model,
                brandName: selectedItem.value.brandName,
                color: selectedItem.value.color,
                storageGb : selectedItem.value.storageGb,
                quantity: cartQty.value,
                maxQuantity: selectedItem.value.quantity,
                priceEach: selectedItem.value.price
            }, 
            getAccessToken())
        addToCart(addedItem)
    } catch (error) {
        console.log(error);
    }
} 
</script>

<template>
<div class="bg-white text-[#332A1E]">
    <PopupMessage message="The sale item has been updated." :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
    <div v-if="selectedItem?.id && !showNotFound">
        <div class="px-7 md:px-13 pb-15 pt-22 md:pt-30 mx-auto font-rubik relative">
            <router-link :to="{ name: 'SaleItems' }" class="w-fit inline-block">
                <BaseButton :icon="backArrowIcon" text="Back to product list" class="itbms-home-button mb-7"/>
            </router-link>
            <div class="itbms-row flex justify-center flex-col md:flex-row gap-10 xl:gap-12 xl:mx-5">
                <div class="flex flex-col items-center">
                    <div class="bg-[#F0EDEC] w-60 h-60 md:w-70 md:h-70 lg:w-80 lg:h-80 xl:w-110 xl:h-110 rounded-2xl flex items-center justify-center overflow-hidden">
                        <p v-if="!phones.mainImage" class="text-[#332A1E] text-lg xl:text-2xl">No Picture</p>
                        <img v-else :src="phones.mainImage" alt="Selected Phone" class="h-[10rem] md:h-[13rem] lg:h-[16rem] xl:h-[16rem] 2xl:h-[18rem] object-contain">
                    </div>
                    <div>
                        <OptionsPhone :phones="phones.thumbnail" :selectedIndex="selectedPhone"
                            @update:selected-index="changeMainImg" />
                    </div>
                </div>
                <div class="flex flex-col w-full">
                    <div class="border-b-3 border-[#E5E8F4] pb-4.5 space-y-3">
                        <p class="itbms-brand text-[#A4A4A3] text-base lg:text-lg xl:text-xl">{{ selectedItem.brandName }}</p>
                        <p class="itbms-model text-[#332A1E] font-bold text-2xl md:text-3xl xl:text-4xl">{{ selectedItem.model }}</p>
                        <div class="flex gap-2 items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20">
                                <path fill="#332A1E" d="M6.123 7.25L6.914 2H2.8L1.081 6.5q-.08.24-.081.5c0 1.104 1.15 2 2.571 2c1.31 0 2.393-.764 2.552-1.75M10 9c1.42 0 2.571-.896 2.571-2q-.001-.062-.005-.121L12.057 2H7.943l-.51 4.875L7.429 7c0 1.104 1.151 2 2.571 2m5 1.046V14H5v-3.948c-.438.158-.92.248-1.429.248c-.195 0-.384-.023-.571-.049V16.6c0 .77.629 1.4 1.398 1.4H15.6c.77 0 1.4-.631 1.4-1.4v-6.348a4 4 0 0 1-.571.049A4.2 4.2 0 0 1 15 10.046M18.92 6.5L17.199 2h-4.113l.79 5.242C14.03 8.232 15.113 9 16.429 9C17.849 9 19 8.104 19 7q-.001-.26-.08-.5" />
                            </svg>
                            <p class="itbms-nickname font-medium">{{ selectedItem.seller.userName }}</p>
                        </div>
                        <p class="itbms-description text-[#6F879C] text-sm md:text-base lg:text-lg font-light">{{ selectedItem.description }}</p>
                    </div>
                    <div class="flex justify-between border-b-3 py-5 gap-3 border-[#E5E8F4] flex-col lg:flex-row">
                        <p class="text-[#6F879C] font-bold text-2xl md:text-2xl lg:text-3xl xl:text-4xl">
                            <span class="itbms-price-unit pr-2">Bath</span>
                            <span class="itbms-price">{{ selectedItem.price?.toLocaleString() }}</span>
                        </p>
                        <div class="flex items-center">
                            <img :src="boxImg" alt="box" class="w-7 xl:w-8 mr-3">
                            <p :class="[selectedItem.quantity > 0 ? 'bg-[#97C5B8] text-[#225528]' : 'bg-[#EAA9A9] text-[#680D0D]']"
                                class="p-2 font-bold text-sm lg:text-base rounded-full px-7">
                                <span class="itbms-quantity mr-2">{{ selectedItem.quantity }}</span>
                                <span class="itbms-quantity-unit">items in stock</span>
                            </p>
                        </div>
                    </div>
                    <div class="text-[#332A1E] pb-3">
                        <p class="pt-4.5 font-bold text-xl lg:text-2xl">Product Description</p>
                        <ItemDetailRow label="Brand" :value="selectedItem.brandName" />
                        <ItemDetailRow label="Model" :value="selectedItem.model" />
                        <ItemDetailRow label="StorageGb" :value="selectedItem.storageGb" unit="GB" valueClass="itbms-storageGb" unitClass=" itbms-storageGb-unit" />
                        <ItemDetailRow label="RamGb" :value="selectedItem.ramGb" unit="GB" valueClass=" itbms-ramGb" unitClass="itbms-ramGb-unit" />
                        <ItemDetailRow label="ScreenSizeInch" :value="selectedItem.screenSizeInch" unit="Inches" valueClass="itbms-screenSizeInch" unitClass="itbms-screenSizeInch-unit" />
                        <ItemDetailRow label="Color" :value="selectedItem.color" valueClass="itbms-color" />
                    </div>
                    <div v-if="getUserId() === selectedItem?.seller?.id" class="flex justify-center items-center gap-10 m-4">
                        <router-link :to="{ name: 'EditSaleItem', params: { id: saleItemId } }">
                            <BaseButton :icon="editIcon" text="Edit" class="itbms-edit-button"/>
                        </router-link>
                        <BaseButton @click="showDelConfirm = true" :icon="trashIcon" text="Delete" textColor="text-[#D27B7B]" borderColor="border-[#D27B7B]" class="itbms-delete-button"/>
                    </div>
                    <div class="flex my-4 text-sm md:text-base lg:text-lg justify-center gap-7">
                        <div class="flex gap-5 items-center border-2 border-[#6F879C] rounded-md">
                            <button @click="decCartQty" class="itbms-dec-qty-button py-2.5 px-4 border-r border-[#6F879C] text-[#6F879C] font-medium" :class="cartQty === 1 ? 'text-gray-400 cursor-not-allowed' : 'hover:bg-[#6F879C]/15'">-</button>
                            <p class="itbms-add-to-cart-quantity">{{ cartQty }}</p>
                            <button @click="incCartQty" class="itbms-inc-qty-button py-2.5 px-4 border-l border-[#6F879C] text-[#6F879C] font-medium" :class="cartQty === selectedItem.quantity ? 'text-gray-400 cursor-not-allowed' : 'hover:bg-[#6F879C]/15'">+</button>
                        </div>
                        <BaseButton @click="addItemToCart" :icon="cart" text="Add to Cart" bg-color="bg-[#6F879C]" text-color="text-[#FFFFFF]" class="itbms-add-to-cart-button w-full" :disabled="selectedItem.quantity <= 0"/>
                    </div>
                    <div class="text-xs lg:text-sm space-y-1 md:gap-7 flex items-center flex-col md:flex-row justify-between mt-3">
                        <div class="flex gap-1">
                            <p class="font-semibold text-[#332A1E]/70 mr-3">Created On:</p>
                            <p class="text-[#332A1E]/40">{{ selectedItem.createdOn }}</p>
                        </div>
                        <div class="flex gap-1">
                            <p class="font-semibold text-[#332A1E]/70 mr-3">Updated On:</p>
                            <p class="text-[#332A1E]/40">{{ selectedItem.updatedOn }}</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" message="Do you want to delete this sale item?" @delete="deleteSaleItem"/>
    </div>
    <ErrorMessage title="Sale Item" description="The requested sale item does not exist." backPathName="SaleItems" v-else :img="productNotFound">
        <span>Sale Item</span><br/>
        <span>Not Found</span>
    </ErrorMessage>
</div>
</template>

<style scoped></style>
