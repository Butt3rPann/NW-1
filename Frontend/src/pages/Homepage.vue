<script setup>
import Bag from '@/assets/images/bag.png'
import ValueProps from '@/components/home/ValueProps.vue'
import Genuine from '@/assets/images/genuine.png'
import SaleItemCard from '@/components/sale-item/SaleItemCard.vue'
import { getItems } from '@/libs/fetchUtils'
import { onMounted, ref,computed} from 'vue'
import Shipping from '@/assets/images/shipping.png'
import Ticket from '@/assets/images/ticket.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import Seller from '@/assets/images/seller.png'
import rightVector from '@/assets/images/rigt-vector.png'

const saleItems = ref([])

onMounted(async () => {
    try {
        saleItems.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
        saleItems.value = saleItems.value.slice(-4).reverse()
    } catch (error) {
        console.log(error);
    }
})
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
                    <router-link :to="{ name: 'SaleItemsList' }">
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
        <div class="bg-[#ABBCC9] mx-auto h-fit flex flex-col justify-center pt-11 md:pt-14 lg:pt-19 xl:pt-21 pb-12 px-5 md:px-9 lg:px-12 xl:px-22 gap-5 md:gap-3 lg:gap-4 xl:gap-5">
            <div class="flex justify-between items-center">
                <p class="text-white font-bold text-lg md:text-[1.75rem] lg:text-[2.5rem] text-shadow-lg">Recommended Products</p>
                <router-link :to="{ name: 'SaleItems' }" class="flex items-center gap-2 pt-1 md:pt-0 lg:pt-0">
                    <p class="font-medium text-xs md:text-base lg:text-xl text-white">view all</p>
                    <img :src="rightVector" alt="Icon" class="w-3 md:w-4 lg:w-5"/>
                </router-link>
            </div>
            <SaleItemCard :saleItems="saleItems" view="gallery" class="w-full md:my-[0.5rem] md:gap-4 lg:gap-5 xl:gap-13 grid-cols-2 lg:grid-cols-4"/>
        </div>
    </div>
</template>
 
<style scoped>
</style>
