<script setup>
import Bag from '@/assets/images/bag.png'
import ValueProps from '@/components/home/ValueProps.vue'
import Genuine from '@/assets/images/genuine.png'
import SaleItemCard from '@/components/sale-item/SaleItemCard.vue'
import { getItems } from '@/libs/fetchUtils'
import { onMounted, ref} from 'vue'
import Shipping from '@/assets/images/shipping.png'
import Ticket from '@/assets/images/ticket.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import Seller from '@/assets/images/seller.png'

const saleItems = ref([])

onMounted(async () => {
    try {
        saleItems.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
        saleItems.value = saleItems.value.slice(-5).reverse()
    } catch (error) {
        console.log(error);
    }
})
</script>
 
<template>
    <div class="w-full font-rubik bg-white">
        <div class="relative bg-white h-145 pt-35 px-22">
            <div class="absolute right-0 bottom-0 h-110 w-screen bg-no-repeat bg-[url('@/assets/images/phoneBanner.png')] bg-contain bg-right grayscale pointer-events-none"></div>
            <div class="space-y-6 text-[#332A1E]">
                <p class="font-bold  text-6xl leading-17">
                    <span>Discover top-quanlity products</span><br>
                    <span>at prices you'll love</span>
                </p>
                <p class="text-2xl leading-8">
                    <span>Shop a wide variety of items with special promotions and</span><br>
                    <span>free nationwide delivery.</span>
                </p>
                <div class="flex gap-5">
                    <router-link :to="{ name: 'SaleItems' }">
                        <BaseButton :icon="Bag" text="SHOP NOW" textColor="text-[#F0EDEC]" bgColor="bg-[#6F879C]" class="itbms-shopnow"/>
                    </router-link>
                    <router-link :to="{ name: 'SaleItemsList' }">
                        <BaseButton :icon="Seller" text="SELLER" textColor="text-[#F0EDEC]" bgColor="bg-[#6F879C]" class="itbms-seller"/>
                    </router-link>
                </div>

            </div>
            <div class="absolute bottom-[-7vw] left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex gap-[3vw] z-10">
                <ValueProps :icon="Genuine"><template #text>Guaranteed 100% Genuine</template></ValueProps>
                <ValueProps :icon="Shipping"><template #text>Free nationwide shipping</template></ValueProps>
                <ValueProps :icon="Ticket"><template #text>Discover amazing deals and unbeatable discounts</template></ValueProps>
            </div>
        </div>

        <div class="bg-[#ABBCC9] h-145 px-22 flex flex-col justify-center pt-7 gap-7">
            <div class="flex justify-between items-center">
                <p class="text-white font-bold text-[2.5rem] text-shadow-lg">Recommended Products</p>
                <router-link :to="{ name: 'SaleItems' }" class="flex items-center gap-2">
                    <p class="font-medium text-xl text-white">view all</p>
                    <img src="../assets/images/rigt-vector.png" alt="Icon" class="w-5"/>
                </router-link>
            </div>
            <SaleItemCard :saleItems="saleItems" view="gallery" />
        </div>
    </div>
</template>
 
<style scoped>
</style>
