<script setup>
import { useRoute } from 'vue-router'
import { ref } from 'vue'
import logoImg from '@/assets/images/logo.png'
import profileImg from '@/assets/images/profile.png'

const route = useRoute()

const navItems = [
    { name: 'Home', pathname: 'Homepage' },
    { name: 'Products', pathname: 'SaleItems' },
    { name: 'Promotions', pathname: '' },
    { name: 'About Us', pathname: '' },
    { name: 'Contact Us', pathname: '' }
]

const isMenuOpen = ref(false)
</script>

<template>
    <div class="fixed top-0 w-full z-50 font-rubik bg-[#6F879C] text-[#F0EDEC] border-b border-[#8ea2b4] h-[3.5rem] md:h-[4.5rem] lg:h-[5rem] flex items-center px-5 md:px-7 lg:px-10 justify-between">
        <router-link to="/">
            <div class="flex items-center">
                <img :src="logoImg" alt="logo" class="w-7 md:w-10">
                <p class="font-bold text-base md:text-lg lg:text-xl xl:text-[1.5rem]">ITB-MSHOP</p>
            </div>    
        </router-link>
        <div class="space-y-1 md:hidden" @click="isMenuOpen = !isMenuOpen">
            <div class="menuIcon"></div>
            <div class="menuIcon"></div>
            <div class="menuIcon"></div>
        </div>
        <div class="hidden md:flex md:items-center text-xs md:text-sm lg:text-base xl:text-lg font-medium md:gap-4 lg:gap-10 xl:gap-14">
            <router-link v-for="item in navItems" :key="item.name" :to="{ name: item.pathname }"
                class="p-1 transition-all duration-150" 
                :class="route.name === item.pathname
                    ? 'border-b-2'
                    : 'hover:border-b-2'">
                {{ item.name }}
            </router-link>
        </div>
        <router-link to="/registers">
            <img :src="profileImg" alt="profile" class="w-12 hidden md:block">
        </router-link>
        <div v-if="isMenuOpen" class="absolute left-0 top-14 bg-[#6F879C] text-xs md:hidden font-medium flex items-center flex-col w-full gap-3 py-4">
            <router-link v-for="item in navItems" :key="item.name" :to="{ name: item.pathname }" @click="isMenuOpen = false" class="w-full flex justify-center transition-all duration-150">
                <span class="inline-block transition-all duration-150 p-1"  :class="{'border-b-2 border-white' : route.name === item.pathname}">
                    {{ item.name }}
                </span>
            </router-link>
        </div>
    </div>
    <div v-if="isMenuOpen" class="fixed w-screen h-full bg-black opacity-35 z-40" @click="isMenuOpen = false"/>
</template>

<style scoped>
.menuIcon {
    width: 20px;
    height: 2px;
    background-color: #F0EDEC;
}
</style>