<script setup>
import Bag from '@/assets/images/bag.png'
import ValueProps from '@/components/home/ValueProps.vue'
import Genuine from '@/assets/images/genuine.png'
import SaleItemCard from '@/components/sale-item/SaleItemCard.vue'
import { getItems } from '@/libs/fetchUtils'
import { onMounted, ref} from 'vue'
import Shipping from '@/assets/images/shipping.png'
import Ticket from '@/assets/images/ticket.png'
import Seller from '@/assets/images/seller.png'
import rightVector from '@/assets/images/rigt-vector.png'
import { useUserStore } from '@/stores/UserStore'
import router from '@/router'

const saleItems = ref([])
const saleItemImgs = ref([])

const userStore = useUserStore()
const { getUserType } = userStore

onMounted(async () => {
    try {
        saleItems.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
        saleItems.value = saleItems.value.slice(-8).reverse()
        saleItemImgs.value = saleItems.value.map(item => item.saleItemImages[0]?.fileName)
        
    } catch (error) {
        console.log(error);
    }
})

const brands = ['Apple', 'Samsung', 'Xiaomi', 'OPPO', 'Huawei']

const filterByBrand = (brand) => {
    router.push({name: 'SaleItems', query: {brand}})
}
</script>
 
<template>
    <div class="w-full font-rubik bg-white">
        <div class="relative max-w-screen mx-auto bg-white h-65 md:h-105 lg:h-135 xl:h-145 pt-20 md:pt-30 lg:pt-32 xl:pt-35 px-5 md:px-9 lg:px-12 xl:px-22">
            <div class="absolute right-0 bottom-0 w-full bg-no-repeat bg-[url('@/assets/images/phoneBanner.png')] bg-contain bg-right grayscale pointer-events-none h-35 md:h-63 md:bg-contain lg:h-95 xl:h-110"></div>
            <div class="flex flex-col gap-2 md:gap-4 xl:gap-6 text-[#332A1E]">
                <p class="font-bold text-lg leading-7 md:text-4xl md:leading-10 lg:text-5xl xl:text-6xl lg:leading-17">
                    <span>Discover top-quality products</span><br>
                    <span>at prices you'll love</span>
                </p>
                <div class="w-50 md:w-105 lg:w-140 xl:w-170">
                    <p class="text-[9px] leading-4 md:text-xl md:leading-7 lg:text-xl xl:text-2xl lg:leading-9">
                        <span>Shop a wide variety of items with special promotions and </span>
                        <span>free nationwide delivery</span>
                    </p>
                </div>
                <div class="flex gap-2 md:gap-4 lg:gap-5">
                    <router-link :to="{ name: 'SaleItems' }">
                        <button class="itbms-shopnow text-[#F0EDEC] bg-[#6F879C] flex items-center justify-center w-fit py-2 px-2 md:py-3 md:px-3 lg:py-4 lg:px-4 rounded-md">
                            <img :src="Bag" alt="Bag" class="w-3 md:w-5 mr-1">
                            <p class="font-medium md:font-semibold text-[9px] md:text-sm lg:text-base">SHOP NOW</p>
                        </button>
                    </router-link>
                    <router-link :to="{ name: 'SaleItemsList' }" v-if="getUserType() === 'SELLER'">
                        <button class="itbms-shopnow text-[#F0EDEC] bg-[#6F879C] flex items-center justify-center w-fit py-2 px-2 md:py-3 md:px-3 lg:py-4 lg:px-4 rounded-md">
                            <img :src="Seller" alt="Seller" class="w-3 md:w-5 mr-1">
                            <p class="font-medium md:font-semibold text-[9px] md:text-sm lg:text-base">SELLER</p>
                        </button>
                    </router-link>
                </div> 
            </div>
            <div class="absolute bottom-[-2.2rem] md:bottom-[-3.5rem] lg:bottom-[-5.5rem] xl:bottom-[-6rem] left-1/2 transform -translate-x-1/2 -translate-y-1/2  flex flex-wrap justify-center gap-3 md:gap-5 lg:gap-5 w-full ">
                <ValueProps :icon="Genuine"><template #text>Guaranteed 100% Genuine</template></ValueProps>
                <ValueProps :icon="Shipping"><template #text>Free nationwide shipping</template></ValueProps>
                <ValueProps :icon="Ticket"><template #text>Discover amazing deals and unbeatable discounts</template></ValueProps>
            </div>
        </div>
        <div class="bg-[#ABBCC9] mx-auto pt-11 md:pt-14 lg:pt-19 xl:pt-21 px-5 md:px-9 lg:px-12 xl:px-22 pb-12">
            <p class="text-white font-bold text-lg md:text-2xl lg:text-3xl xl:text-4xl text-shadow-lg text-center">Popular Brands</p>
            <div class="flex justify-center items-center gap-5 lg:gap-10 xl:gap-15 mt-7 md:mt-10 flex-wrap">
                <div v-for="brand in brands" class="flex flex-col items-center gap-3 bg-white shadow-md px-7 py-2 rounded-md hover:scale-[1.01] cursor-pointer" @click="filterByBrand(brand)">
                    <div class="w-10 h-10 md:w-10 md:h-15 lg:w-20 lg:h-20 xl:w-30 xl:h-30 flex justify-center items-center ">
                        <img :src="`./brandImage/${brand}.png`" alt="logo" class="max-w-10 max-h-7 md:max-w-15 md:max-h-7 lg:max-w-22 lg:max-h-12 xl:max-w-25 xl:max-h-15">
                    </div>
                    <p class="p-1 text-xs md:text-sm lg:text-base">{{ brand }}</p>
                </div>
            </div>
        </div>
        <div class=" mx-auto h-fit flex flex-col justify-center pt-11 md:pt-14 lg:pt-19 xl:pt-21 pb-20 px-5 md:px-9 lg:px-12 xl:px-22 gap-5 md:gap-3 lg:gap-4 xl:gap-5">
            <div class="flex justify-between items-center">
                <p class="font-bold text-lg md:text-2xl lg:text-3xl xl:text-4xl">Latest Products</p>
                <router-link :to="{ name: 'SaleItems' }" class="flex items-center gap-2 pt-1 md:pt-0 lg:pt-0">
                    <p class="font-medium text-xs md:text-base lg:text-xl text-[#6F879C]">view all</p>
                    <div class="bg-[#6F879C]/50 px-1.5 py-[7px] rounded-full">
                        <img :src="rightVector" alt="Icon" class="w-3 md:w-4 lg:w-4"/>
                    </div>
                </router-link>
            </div>
            <SaleItemCard v-if="saleItemImgs.length > 0" :saleItems="saleItems" :images="saleItemImgs" view="gallery" class="w-full md:my-[0.5rem] md:gap-4 lg:gap-5 xl:gap-13 grid-cols-2 lg:grid-cols-4"/>
            <div v-else class="flex flex-col items-center py-10">
                <img src="../assets/images/emptySaleItems.png" alt="EmptySaleItems" class=" w-15">
                <p class="text-sm text-[#6F879C]">no sale item</p>
            </div>
        </div>
        <div class="mx-auto h-fit flex flex-col justify-center pb-20 px-5 md:px-9 lg:px-12 xl:px-22 gap-5 md:gap-3 lg:gap-4 xl:gap-5">
            <p class="font-bold text-lg md:text-2xl lg:text-3xl xl:text-4xl">Why Shop With Us?</p>
            <div class="space-y-6">
                <div class="grid grid-cols-1 md:grid-cols-2 gap-6">
                    <div class="bg-[#ABBCC9]/20 rounded-lg p-6 flex justify-between flex-col h-50">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 256 256" class="w-10 h-10">
                            <path fill="#6F879C" d="M244.8 150.4a8 8 0 0 1-11.2-1.6A51.6 51.6 0 0 0 192 128a8 8 0 0 1-7.37-4.89a8 8 0 0 1 0-6.22A8 8 0 0 1 192 112a24 24 0 1 0-23.24-30a8 8 0 1 1-15.5-4A40 40 0 1 1 219 117.51a67.94 67.94 0 0 1 27.43 21.68a8 8 0 0 1-1.63 11.21M190.92 212a8 8 0 1 1-13.84 8a57 57 0 0 0-98.16 0a8 8 0 1 1-13.84-8a72.06 72.06 0 0 1 33.74-29.92a48 48 0 1 1 58.36 0A72.06 72.06 0 0 1 190.92 212M128 176a32 32 0 1 0-32-32a32 32 0 0 0 32 32m-56-56a8 8 0 0 0-8-8a24 24 0 1 1 23.24-30a8 8 0 1 0 15.5-4A40 40 0 1 0 37 117.51a67.94 67.94 0 0 0-27.4 21.68a8 8 0 1 0 12.8 9.61A51.6 51.6 0 0 1 64 128a8 8 0 0 0 8-8" />
                        </svg>
                        <div>
                            <p class="text-lg xl:text-xl pb-3 font-medium">Active Users 10K+</p>
                            <p class="text-xs xl:text-sm">Over 10,000 active users every month — a community you can trust.</p>
                        </div>
                    </div>
                    <div class="bg-[#ABBCC9]/20 rounded-lg p-6 flex justify-between flex-col h-50">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 2048 2048" class="w-10 h-10">
                            <path fill="#6F879C" d="m960 120l832 416v1040l-832 415l-832-415V536zm625 456L960 264L719 384l621 314zM960 888l238-118l-622-314l-241 120zM256 680v816l640 320v-816zm768 1136l640-320V680l-640 320z" />
                        </svg>
                        <div>
                            <p class="text-lg xl:text-xl pb-3 font-medium">15K+ Phones Sold</p>
                            <p class="text-xs xl:text-sm">More than 15,000 phones sold nationwide.</p>
                        </div>
                    </div>
                </div>
                <div class="grid grid-cols-1 md:grid-cols-3 gap-6">
                    <div class="bg-[#ABBCC9]/20 rounded-lg p-6 flex justify-between flex-col h-50">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 512 512" class="w-10 h-10">
                            <path fill="#6F879C" d="m328.375 384l3.698 74.999l-75.862-52.719l-76.287 52.769L183.625 384h-32.039l-5.522 112h36.692l73.413-50.78L329.242 496h36.694l-5.522-112zm87.034-229.086l-2.194-48.054L372.7 80.933l-25.932-40.519l-48.055-2.2L256 16.093l-42.713 22.126l-48.055 2.2L139.3 80.933L98.785 106.86l-2.194 48.054l-22.127 42.714l22.127 42.715l2.2 48.053l40.509 25.927l25.928 40.52l48.055 2.195L256 379.164l42.713-22.126l48.055-2.195l25.928-40.52l40.518-25.923l2.195-48.053l22.127-42.715Zm-31.646 76.949L382 270.377l-32.475 20.78l-20.78 32.475l-38.515 1.76L256 343.125l-34.234-17.733l-38.515-1.76l-20.78-32.475L130 270.377l-1.759-38.514l-17.741-34.235l17.737-34.228L130 124.88l32.471-20.78l20.78-32.474l38.515-1.76L256 52.132l34.234 17.733l38.515 1.76l20.78 32.474L382 124.88l1.759 38.515l17.741 34.233Z" />
                        </svg>
                        <div>
                            <p class="text-lg xl:text-xl pb-3 font-medium">Trusted Partners 100+</p>
                            <p class="text-xs xl:text-sm">Working with verified and trusted sellers.</p>
                        </div>
                    </div>
                    <div class="bg-[#ABBCC9]/20 rounded-lg p-6 flex justify-between flex-col h-50">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 24 24" class="w-8 h-8">
                            <g fill="none" stroke="#6F879C" stroke-linecap="round" stroke-linejoin="round" stroke-width="2">
                                <path d="m3 17l6-6l4 4l8-8" />
                                <path d="M17 7h4v4" />
                            </g>
                        </svg>
                        <div>
                            <p class="text-lg xl:text-xl pb-3 font-medium">Customer Satisfaction 95%</p>
                            <p class="text-xs xl:text-sm">95% of customers are happy with our quality and service.</p>
                        </div>
                    </div>
                    <div class="bg-[#ABBCC9]/20 rounded-lg p-6 flex justify-between flex-col h-50">
                        <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 16 16" class="w-10 h-10">
                            <path fill="#6F879C" d="M10.5 10a.5.5 0 0 0 0 1h2a.5.5 0 0 0 0-1zM1 5.5A2.5 2.5 0 0 1 3.5 3h9A2.5 2.5 0 0 1 15 5.5v5a2.5 2.5 0 0 1-2.5 2.5h-9A2.5 2.5 0 0 1 1 10.5zM14 6v-.5A1.5 1.5 0 0 0 12.5 4h-9A1.5 1.5 0 0 0 2 5.5V6zM2 7v3.5A1.5 1.5 0 0 0 3.5 12h9a1.5 1.5 0 0 0 1.5-1.5V7z" />
                        </svg>
                        <div>
                            <p class="text-lg xl:text-xl pb-3 font-medium">Secure Payment 100%</p>
                            <p class="text-xs xl:text-sm">Shop with confidence using our secure and encrypted payment system.</p>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>
 
<style scoped>
</style>
