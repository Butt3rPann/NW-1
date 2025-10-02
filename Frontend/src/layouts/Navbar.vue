<script setup>
import { useRoute } from 'vue-router'
import { ref } from 'vue'
import logoImg from '@/assets/images/logo.png'
import profileImg from '@/assets/images/profile.png'
import { useUserStore } from '@/stores/UserStore'
import { storeToRefs } from 'pinia'
import router from '@/router'

const userStore = useUserStore()
const { getNickname, removeAccessToken, getCartItemCount } = userStore
const { nickName } = storeToRefs(userStore)

const route = useRoute()

const navItems = [
    { name: 'Home', pathname: 'Homepage' },
    { name: 'Products', pathname: 'SaleItems' },
    { name: 'Promotions', pathname: '' },
    { name: 'About Us', pathname: '' },
    { name: 'Contact Us', pathname: '' }
]

const isMenuOpen = ref(false)

const handleLogout = () => {
   removeAccessToken()
   router.push({ name: 'SaleItems' })  
}
</script>

<template>
    <div class="fixed top-0 w-full z-50 font-rubik bg-[#6F879C] text-[#F0EDEC] border-b border-[#8ea2b4] h-[3.5rem] md:h-[4.5rem] lg:h-[5rem] flex items-center px-5 md:px-7 lg:px-10 justify-between">
        <router-link to="/">
            <div class="flex items-center">
                <img :src="logoImg" alt="logo" class="w-7 md:w-10">
                <p class="font-bold text-base md:text-lg lg:text-xl xl:text-[1.5rem]">ITB-MSHOP</p>
            </div>    
        </router-link>
        <div class="space-y-1 md:hidden py-2 px-1" @click="isMenuOpen = !isMenuOpen">
            <div class="menuIcon"></div>
            <div class="menuIcon"></div>
            <div class="menuIcon"></div>
        </div>
        <div class="hidden md:flex md:items-center text-xs md:text-sm lg:text-base xl:text-lg font-medium md:gap-4 lg:gap-10 xl:gap-12">
            <router-link v-for="item in navItems" :key="item.name" :to="{ name: item.pathname }"
                class="p-1 transition-all duration-150" 
                :class="route.name === item.pathname
                    ? 'border-b-2'
                    : 'hover:border-b-2'">
                {{ item.name }}
            </router-link>
        </div>
        <div class="hidden md:flex gap-3 font-medium" v-if="getNickname() === ''">
            <router-link to="/signin">
                <p>Login</p>
            </router-link>
            <p>|</p>
            <router-link to="/registers">
                <p>Signup</p>
            </router-link>
        </div>
        <div v-else class="hidden md:flex gap-7 font-medium items-center">
            <router-link :to="{ name: 'Cart' }">
                <div class="relative">
                    <p class="absolute -top-1.5 -right-1.5 px-[0.30rem] rounded-full bg-[#D27B7B] text-[#F0EDEC] text-xs">{{ getCartItemCount() }}</p>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                        <path fill="#F0EDEC" d="M17 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2M1 2v2h2l3.6 7.59l-1.36 2.45c-.15.28-.24.61-.24.96a2 2 0 0 0 2 2h12v-2H7.42a.25.25 0 0 1-.25-.25q0-.075.03-.12L8.1 13h7.45c.75 0 1.41-.42 1.75-1.03l3.58-6.47c.07-.16.12-.33.12-.5a1 1 0 0 0-1-1H5.21l-.94-2M7 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2" />
                    </svg>
                </div>
            </router-link>
            <router-link :to="{ name: 'Profile' }" class="itbms-profile hidden md:flex justify-center items-center gap-2">
                <img :src="profileImg" class="w-10"/>
		        <p>{{ nickName }}</p>
            </router-link>
            <div @click="handleLogout" class="itbms-logout border py-1 px-2 rounded-md cursor-pointer">
                Logout
            </div>
        </div>
        <div v-if="isMenuOpen" class="absolute left-0 top-14 bg-[#6F879C] text-xs md:hidden font-medium flex items-center flex-col w-full gap-3 py-5">
            <router-link v-for="item in navItems" :key="item.name" :to="{ name: item.pathname }" @click="isMenuOpen = false" class="w-full flex justify-center transition-all duration-150">
                <span class="inline-block transition-all duration-150 p-1"  :class="{'border-b-2 border-white' : route.name === item.pathname}">
                    {{ item.name }}
                </span>
            </router-link>
            <div class="flex justify-center items-center gap-3 font-medium" v-if="getNickname() === ''">
                <router-link to="/signin" @click="isMenuOpen = false"  class="px-4 py-1 bg-white text-[#6F879C] rounded hover:bg-gray-200 transition-colors">
                    Login
                </router-link>
                <router-link to="/registers" @click="isMenuOpen = false"  class="px-4 py-1 bg-white text-[#6F879C] rounded hover:bg-gray-200 transition-colors">
                    Signup
                </router-link>
            </div>
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
