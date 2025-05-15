<script setup>
import backArrowIcon from '@/assets/images/backArrow.png'
import homeLogo from '@/assets/images/home.png'
import BaseButton from '../elements/BaseButton.vue';

</script>
 
<template>
    <div class="flex flex-row items-center justify-center h-screen bg-white font-rubik">
        <img src="../../assets/images/product-not-found.png"
            class="w-170 h-auto mr-17" />
        <div class="text-left">
            <p class="text-[3.5rem] font-bold text-[#332A1E] leading-tight">
                <span>Sale Item</span><br/>
                <span>Not Found</span>
            </p>
            <p class="itbms-message text-[1.4rem] text-[#332A1E] mt-3 mb-6">The requested sale item does not exist.</p>
            <div class="flex flex-row gap-6">
                <router-link :to="{ name: 'Homepage' }">
                    <BaseButton :icon="homeLogo" text="Back to homepage" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]"/>
                </router-link>
                <router-link :to="{ name: 'SaleItems' }">
                    <BaseButton :icon="backArrowIcon" text="Back to product list" class="itbms-button"/>
                </router-link>
            </div>
        </div>
    </div>
</template>
 
<style scoped>

</style>