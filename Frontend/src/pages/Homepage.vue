<script setup>
import BaseButton from '@/components/BaseButton.vue'
import Bag from '@/assets/images/Bag.png'
import ValueProps from '@/components/home/ValueProps.vue';
import Genuine from '@/assets/images/genuine.png'


</script>
 
<template>
    <div>
        <div class="relative bg-white h-[37vw] w-screen font-rubik pt-[9.5vw] px-[6vw]">
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
                <ValueProps :icon="Genuine"><template #text>Free nationwide shipping</template></ValueProps>
                <ValueProps :icon="Genuine"><template #text>Discover amazing deals and unbeatable discounts</template></ValueProps>
            </div>
        </div>
        <div class="bg-red-500 h-[10vw]" >
            NENE
        </div>
    </div>
</template>
 
<style scoped>

</style>