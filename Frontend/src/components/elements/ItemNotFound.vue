<script setup>
import backArrowIcon from '@/assets/images/backArrow.png'
import homeLogo from '@/assets/images/home.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import productNotFound from '@/assets/images/product-not-found.png'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const props = defineProps({
    title: {
        type: String,
        required: true
    },
    description: {
        type: String,
        required: true
    },
    backPathName: {
        type: String,
        required: true
    },
})

function goBack() {
    if (route.name === props.backPathName) router.go(0)
    else router.push({ name: props.backPathName })
}
</script>
 
<template>
    <div class="flex flex-row items-center justify-center h-screen bg-white font-rubik">
        <img :src="productNotFound"
            class="w-170 h-auto mr-17" />
        <div class="text-left">
            <p class="text-[3.5rem] font-bold text-[#332A1E] leading-tight">
                <span>{{ title }}</span><br/>
                <span>Not Found</span>
            </p>
            <p class="itbms-message text-[1.4rem] text-[#332A1E] mt-3 mb-6">{{ description }}</p>
            <div class="flex flex-row gap-6">
                <router-link :to="{ name: 'Homepage' }">
                    <BaseButton :icon="homeLogo" text="Back to homepage" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]"/>
                </router-link>
                <BaseButton @click="goBack" :icon="backArrowIcon" :text="`Back to ${title.toLowerCase()} list`" class="itbms-button"/>
            </div>
        </div>
    </div>
</template>
 
<style scoped>

</style>