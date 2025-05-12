<script setup>
import { useRoute } from 'vue-router'

const route = useRoute()

const navItems = [
    { name: 'Home', path: '/' },
    { name: 'Products', path: '/sale-items' },
    { name: 'Promotions', path: '' },
    { name: 'About Us', path: '' },
    { name: 'Contact Us', path: '' }
]
</script>

<template>
    <div class="fixed top-0 left-0 w-full z-50 font-rubik bg-[#6F879C] text-[#F0EDEC] h-[5rem] flex items-center px-7 justify-between">
        <router-link to="/">
            <div class="flex items-center">
                <img src="@/assets/images/logo.png" alt="logo" class="w-10">
                <p class="font-bold text-[1.4rem]">ITB-MSHOP</p>
            </div>    
        </router-link>
        <div class="flex items-center text-lg font-medium">
            <router-link v-for="item in navItems" :key="item.path" :to="item.path"
                class="m-[2.5vw] p-[0.3vw] transition-all duration-150" 
                :class="[route.path === item.path
                    ? 'border-b-[0.18vw]'
                    : 'hover:border-b-[0.18vw]',
                    item.path === '/sale-items' ? 'itbms-home-button' : '']">
                {{ item.name }}
            </router-link>
        </div>
        <img src="@/assets/images/profile.png" alt="profile" class="w-12">
    </div>
</template>

<style scoped></style>