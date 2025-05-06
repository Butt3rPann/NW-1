<script setup>
import backArrow from '@/assets/images/backArrow.png'
import homeLogo from '@/assets/images/home.png'
import LinkButton from '../elements/LinkButton.vue';

</script>
 
<template>
    <div class="flex flex-row items-center justify-center h-screen bg-white font-rubik">
        <img src="../../assets/images/product-not-found.png" alt="Product Not Found"
            class="w-170 h-auto mr-17" />
        <div class="text-left">
            <p class="text-[3.5rem] font-bold text-[#332A1E] leading-tight">
                <span>Product</span><br />
                <span>Not Found</span>
            </p>
            <p class="itbms-message text-[1.4rem] text-[#332A1E] mt-3 mb-6">The requested sale item does not exist.</p>
            <div class="flex flex-row gap-6">
                <LinkButton :icon="homeLogo" alt="homeLogo" text="Back to homepage" :to="'/'" 
                    textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" />
                <LinkButton :icon="backArrow" alt="backArrow" text="Back to product list" :to="'/sale-items'"
                    textColor="text-[#6F879C]" bgColor="bg-transparent"
                    class="itbms-button" />
            </div>
        </div>
    </div>
</template>
 
<style scoped>

</style>