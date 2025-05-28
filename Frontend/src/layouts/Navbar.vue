<script setup>
import { useRoute } from 'vue-router'
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
</script>

<template>
    <div class="fixed top-0 left-0 w-full z-50 font-rubik bg-[#6F879C] text-[#F0EDEC] h-[5rem] flex items-center px-7 justify-between">
        <router-link to="/">
            <div class="flex items-center">
                <img :src="logoImg" alt="logo" class="w-10">
                <p class="font-bold text-[1.4rem]">ITB-MSHOP</p>
            </div>    
        </router-link>
        <div class="flex items-center text-lg font-medium gap-14">
            <router-link v-for="item in navItems" :key="item.name" :to="{ name: item.pathname }"
                class="p-1 transition-all duration-150" 
                :class="route.name === item.pathname
                    ? 'border-b-2'
                    : 'hover:border-b-2'">
                {{ item.name }}
            </router-link>
        </div>
        <img :src="profileImg" alt="profile" class="w-12">
    </div>
</template>

<style scoped></style>