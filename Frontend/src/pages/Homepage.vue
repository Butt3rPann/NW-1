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
    <div class="w-full font-rubik bg-white ">
        <div class="relative max-w-screen mx-auto bg-white h-69 md:h-110 lg:h-145 pt-26 md:pt-35 lg:pt-35 px-5 md:px-6 xl:px-22">
            <div class="absolute right-0 bottom-0 w-full bg-no-repeat bg-[url('@/assets/images/phoneBanner.png')] bg-contain bg-right grayscale pointer-events-none h-26 md:h-63 md:bg-contain lg:h-110"></div>
            <div class="space-y-2 md:space-y-6 lg:space-y-6 text-[#332A1E] ml-1 md:ml-0 lg:ml-0 ">
                <p class="font-bold text-[17px] leading-7 md:text-4xl md:leading-10 lg:text-6xl lg:leading-17 md:ml-4 lg:ml-0">
                    <span>Discover top-quality products</span><br>
                    <span>at prices you'll love</span>
                </p>

                <div class="h-7 w-50 md:h-10 md:w-105 lg:h-20 lg:w-170">
                    <p class="text-[9px] leading-4 md:text-xl md:leading-7 lg:text-2xl lg:leading-9 md:ml-4 lg:ml-0">
                        <span>Shop a wide variety of items with special promotions and </span>
                        <span>free nationwide delivery</span>
                    </p>
                </div>

                <div class="flex gap-5 md:ml-4" >
                    <router-link :to="{ name: 'SaleItems' }">
                        <BaseButton :icon="Bag" text="SHOP NOW" textColor="text-[#F0EDEC]" bgColor="bg-[#6F879C]" class="itbms-shopnow"/>
                    </router-link>
                    <router-link :to="{ name: 'SaleItemsList' }">
                        <BaseButton :icon="Seller" text="SELLER" textColor="text-[#F0EDEC]" bgColor="bg-[#6F879C]" class="itbms-seller"/>
                    </router-link>
                </div> 

            </div>
            <div class="absolute bottom-[-2.5rem] md:bottom-[-4rem] lg:bottom-[-7rem] left-1/2 transform -translate-x-1/2 -translate-y-1/2  flex flex-wrap justify-center gap-3 md:gap-5 lg:gap-5 w-full ">
                <ValueProps :icon="Genuine"><template #text>Guaranteed 100% Genuine</template></ValueProps>
                <ValueProps :icon="Shipping"><template #text>Free nationwide shipping</template></ValueProps>
                <ValueProps :icon="Ticket"><template #text>Discover amazing deals and unbeatable discounts</template></ValueProps>
            </div>
        </div>

        <div class="bg-[#ABBCC9] mx-auto h-124 lg:h-145 md:h-100 flex flex-col justify-center px-5 pt-4 lg:pt-7 gap-7 md:px-9 lg:px-22">
            <div class="flex justify-between items-center">
                <p class="text-white font-bold text-md md:text-[1.75rem] lg:text-[2.5rem] text-shadow-lg">Recommended Products</p>
                <router-link :to="{ name: 'SaleItems' }" class="flex items-center gap-2 pt-1 md:pt-0 lg:pt-0">
                    <p class="font-medium text-xs md:text-xl lg:text-xl text-white">view all</p>
                    <img :src="rightVector" alt="Icon" class=" w-3 md:w-5 lg:w-5"/>
                </router-link>
            </div>

            <div class="bg-red-200">
                <SaleItemCard :saleItems="saleItems" view="gallery" class=" md:my-[0.5rem] lg:gap-8 " />
            </div>
        </div>
    </div>
</template>
 
<style scoped>
</style>
