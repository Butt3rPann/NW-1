<script setup>
import BaseButton from '@/components/elements/BaseButton.vue';
import { ref } from 'vue';
import { useUserStore } from '@/stores/UserStore';
import { storeToRefs } from 'pinia';

const userStore = useUserStore()
const { removeFromCart, storeCart } = userStore
const { cart } = storeToRefs(userStore)

const checkbox = ref(true)

const decCartQty = (indexOfSeller, indexOfItem) => {
    const quantity = cart.value[indexOfSeller].saleItems[indexOfItem].quantity
    if (quantity !== 1) {
        cart.value[indexOfSeller].saleItems[indexOfItem].quantity -= 1
        storeCart()
    } else {
        removeFromCart(indexOfSeller, indexOfItem)
    }
}

const incCartQty = (indexOfSeller, indexOfItem) => {
    const maxQty = cart.value[indexOfSeller].saleItems[indexOfItem].maxQuantity
    const quantity = cart.value[indexOfSeller].saleItems[indexOfItem].quantity

    if (quantity < maxQty) {
        cart.value[indexOfSeller].saleItems[indexOfItem].quantity += 1
        storeCart()
    }
}
</script>
 
<template>
    <div class="w-full min-h-screen font-rubik flex flex-col items-center text-[#332A1E] gap-10 bg-white pb-20 pt-25 px-10 md:pt-30 md:px-12 lg:pt-35 lg:px-25">
        <p class="text-2xl sm:text-3xl lg:text-4xl font-bold">Shopping Cart</p>
        <div class="flex flex-col md:flex-row w-full gap-5 lg:gap-10 xl:gap-15">
            <div class="md:w-2/3 h-fit space-y-3">
                <div class="flex gap-3 border border-[#332A1E]/10 shadow-sm rounded-md p-3">
                    <label class="inline-flex items-center cursor-pointer">
                        <input type="checkbox" v-model="checkbox" class="hidden peer itbms-select-all">
                        <div class="w-4 h-4 flex-shrink-0 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                            <svg v-if="checkbox" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                                <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                            </svg>
                        </div>
                    </label>
                    <p class="font-medium text-sm xl:text-base">Select All</p>
                </div>
                <div v-for="(seller, indexOfSeller) in cart" :key="indexOfSeller.id" class="itbms-row border border-[#332A1E]/10 shadow-sm rounded-md px-3 pt-3">
                    <div class="flex gap-3 pb-3 border-b border-[#6F879C]/30">
                        <label class="inline-flex items-center cursor-pointer">
                            <input type="checkbox" v-model="checkbox" class="hidden peer itbms-select-nickname">
                            <div class="w-4 h-4 flex-shrink-0 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                                <svg v-if="checkbox" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
                                    <path d="M20.285 6.709a1 1 0 0 0-1.414-1.418l-9.9 9.9-4.242-4.243a1 1 0 0 0-1.415 1.414l4.95 4.95a1 1 0 0 0 1.414 0l10.607-10.603z"/>
                                </svg>
                            </div>
                        </label>
                        <div class="flex gap-2 items-center">
                            <svg xmlns="http://www.w3.org/2000/svg" width="20" height="20" viewBox="0 0 20 20">
                                <path fill="#332A1E" d="M6.123 7.25L6.914 2H2.8L1.081 6.5q-.08.24-.081.5c0 1.104 1.15 2 2.571 2c1.31 0 2.393-.764 2.552-1.75M10 9c1.42 0 2.571-.896 2.571-2q-.001-.062-.005-.121L12.057 2H7.943l-.51 4.875L7.429 7c0 1.104 1.151 2 2.571 2m5 1.046V14H5v-3.948c-.438.158-.92.248-1.429.248c-.195 0-.384-.023-.571-.049V16.6c0 .77.629 1.4 1.398 1.4H15.6c.77 0 1.4-.631 1.4-1.4v-6.348a4 4 0 0 1-.571.049A4.2 4.2 0 0 1 15 10.046M18.92 6.5L17.199 2h-4.113l.79 5.242C14.03 8.232 15.113 9 16.429 9C17.849 9 19 8.104 19 7q-.001-.26-.08-.5" />
                            </svg>
                            <p class="itbms-nickname font-medium text-sm xl:text-base">{{ seller.sellerName }}</p>
                        </div>
                    </div>
                    <div v-for="(item, indexOfItem) in seller.saleItems" :key="indexOfItem.id" class="px-4 pt-4" >
                        <div class="itbms-item-row flex gap-4 items-center min-h-20 pb-4" :class="indexOfItem !== seller.saleItems.length - 1 ? 'border-b border-[#332A1E]/10' : ''">
                            <label class="inline-flex items-center cursor-pointer">
                                <input type="checkbox" v-model="checkbox" class="hidden peer">
                                <div class="w-4 h-4 flex-shrink-0 rounded-sm border border-[#ABBCC9] peer-checked:bg-[#6F879C] peer-checked:border-[#6F879C] flex items-center justify-center transition">
                                    <svg v-if="checkbox" xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" fill="#F2EDEC" class="w-3.5 h-3.5">
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
                                        <button @click="decCartQty(indexOfSeller, indexOfItem)" class="itbms-dec-qty-button px-3 rounded-sm bg-[#f5f0ec] hover:bg-[#ded8d2] cursor-pointer">-</button>
                                        <p class="itbms-item-quantity">{{ item.quantity }}</p>
                                        <button @click="incCartQty(indexOfSeller, indexOfItem)" class="itbms-inc-qty-button px-3 rounded-sm bg-[#f5f0ec] hover:bg-[#ded8d2] cursor-pointer">+</button>
                                    </div>
                                </div>
                            </div>
                        </div>
                    </div>
                </div>
            </div>
            <div class="md:w-1/3 border border-[#332A1E]/10 shadow-sm rounded-md p-4 space-y-5 h-fit">
                <p class="text-lg sm:text-xl lg:text-2xl font-bold text-center">Cart Summary</p>
                <hr class="text-[#332A1E]/10">
                <div class="text-md sm:text-sm lg:text-lg space-y-1">
                    <div class="flex justify-between">
                        <p class="font-medium">Total items :</p>
                        <p class="itbms-total-order-items">2</p>
                    </div>
                    <div class="flex justify-between">
                        <p class="font-medium">Total price :</p>
                        <p class="flex gap-2">
                            <span>Bath</span>
                            <span class="itbms-total-order-price">62,700</span>
                        </p>
                    </div>
                </div>
                <BaseButton text="Place order" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-place-order-button w-full"/>
            </div>
        </div>
    </div>
</template>
 
<style scoped>
</style>