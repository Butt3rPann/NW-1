<script setup>
import BaseButton from '@/components/BaseButton.vue'
import Bag from '@/assets/images/bag.png'
import ValueProps from '@/components/home/ValueProps.vue'
import Genuine from '@/assets/images/genuine.png'
import SaleItemCard from '@/components/sale-item/SaleItemCard.vue'
import { getSaleItems } from '@/libs/fetchUtils'
import { onMounted, ref} from 'vue'
import Shipping from '@/assets/images/shipping.png'
import Ticket from '@/assets/images/ticket.png'

const saleItems = ref([])

onMounted(async () => {
    try {
        saleItems.value = await getSaleItems(`${import.meta.env.VITE_APP_URL}/v1/sale-items`)
        saleItems.value = saleItems.value.slice(-5).reverse()
    } catch (error) {
        console.log(error);
    }
})
</script>
 
<template>
    <div>
        <div class="relative bg-white h-[42vw] w-full font-rubik pt-[9.5vw] px-[6vw]">
            <div class="absolute right-0 bottom-0 h-[31vw] w-screen bg-no-repeat bg-[url('@/assets/images/phoneBanner.png')] bg-contain bg-right grayscale pointer-events-none"></div>
            <div class="space-y-[1.5vw] text-[#332A1E]">
                <p class="font-bold text-[4vw] leading-[4.5vw]">
                    <span>Discover top-quanlity products</span><br>
                    <span>at prices you'll love</span>
                </p>
                <p class="text-[1.7vw] leading-[2.5vw]">
                    <span>Shop a wide variety of items with special promotions and</span><br>
                    <span>free nationwide delivery.</span>
                </p>
                <BaseButton :icon="Bag" text="SHOP NOW" to="/sale-items" textColor="text-[#F0EDEC]" class="itbms-shopnow px-[2vw]"/>
            </div>
            <div class="absolute bottom-[-7vw] left-1/2 transform -translate-x-1/2 -translate-y-1/2 flex gap-[3vw] z-10">
                <ValueProps :icon="Genuine"><template #text>Guaranteed 100% Genuine</template></ValueProps>
                <ValueProps :icon="Shipping"><template #text>Free nationwide shipping</template></ValueProps>
                <ValueProps :icon="Ticket"><template #text>Discover amazing deals and unbeatable discounts</template></ValueProps>
            </div>
        </div>

        <div class="bg-[#ABBCC9] h-[42vw] w-full font-rubik" >
          <div class="relative top-[7.7vw] px-[6vw] w-full">
            <p class="text-[#FFFF] font-bold text-[3vw] leading-[4vw] title-shadow">Recommended Products</p>
            <div class="flex justify-end">
                <router-link to="/sale-items" class="flex items-center">
                    <button class="font-bold text-[1.3vw] text-[#FFFF]">view all</button>
                    <img src="../assets/images/rigt-vector.png" alt="icon" class="w-[1.5vw] h-auto ml-[0.5vw]" />
                </router-link>
            </div>
            <div class="my-[2vw] ">
                <SaleItemCard :saleItems="saleItems" view="gallery" />
            </diV>
          </div>
        </div>
    </div>
</template>
 
<style scoped>
.title-shadow {
    text-shadow: 0 0.2vw 0.5vw rgba(0,0,0,0.5);
}
</style>
