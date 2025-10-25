<script setup>
import backArrowIcon from '@/assets/images/backArrow.png'
import homeLogo from '@/assets/images/home.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import { useRoute, useRouter } from 'vue-router'

const route = useRoute()
const router = useRouter()

const props = defineProps({
    title: String,
    description: {
        type: String,
        required: true
    },
    backPathName: {
        type: String,
        required: true
    },
    img: {
        type: String,
        required: true
    }
})

function goBack() {
    if (route.name === props.backPathName) router.go(0)
    else router.push({ name: props.backPathName })
}
</script>
 
<template>
    <div class="flex flex-col md:flex-row items-center justify-center h-screen bg-white font-rubik px-2 md:px-8 lg:px-12">
        <img :src="img" class="w-60 sm:w-100 md:w-80 lg:w-120 xl:w-140 h-auto md:mr-10 lg:mr-15" />
        <div class="text-center md:text-left">
            <p class="text-2xl sm:text-4xl lg:text-6xl font-bold text-[#332A1E] leading-tight mt-5 md:mt-0">
                <slot></slot>
            </p>
            <p class="itbms-message text-sm sm:text-base lg:text-xl text-[#332A1E] mt-3 mb-6">{{ description }}</p>
            <div class="flex justify-center sm:justify-start flex-row gap-3 md:gap-4 lg:gap-6">
                <router-link :to="{ name: 'Homepage' }">
                    <BaseButton :icon="homeLogo" text="Back to homepage" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="w-50"/>
                </router-link>
                <BaseButton @click="goBack" :icon="backArrowIcon" :text="`Back to ${title.toLowerCase()} list`" class="itbms-button w-50"/>
            </div>
        </div>
    </div>
</template>
 
<style scoped>

</style>