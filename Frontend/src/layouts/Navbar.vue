<script setup>
import { useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import logoImg from '@/assets/images/logo.png'
import profileImg from '@/assets/images/profile.png'
import { useUserStore } from '@/stores/UserStore'
import { storeToRefs } from 'pinia'
import router from '@/router'
import { getItemByIdWithToken } from '@/libs/fetchUtils'

const userStore = useUserStore()
const { getNickname, removeAccessToken, getUserId, getAccessToken, getCartItemCount, getUserType, setSellerOrdersCount } = userStore
const { nickName, cart, sellerOrdersCount } = storeToRefs(userStore)

const route = useRoute()

const navItems = [
    { name: 'Home', pathname: 'Homepage' },
    { name: 'Products', pathname: 'SaleItems' },
    { name: 'Orders', pathname: 'OrderList' },
    { name: 'About Us', pathname: '' },
    { name: 'Contact Us', pathname: '' }
]

const isMenuOpen = ref(false)

const handleLogout = () => {
   removeAccessToken()
   router.push({ name: 'SaleItems' })  
}

onMounted(async () => {
    try {
        const isLoggedIn = !!localStorage.getItem('access_token')
        if (isLoggedIn) {
            const data = await getItemByIdWithToken(`${import.meta.env.VITE_APP_URL}/v2/users/${getUserId()}/carts`, getAccessToken());
            userStore.setCart(data);
        }
        if (getUserType() == 'SELLER') {
            await getSellerOrders()
        }
    } catch (error) {
        console.log(error);
  }
})

async function getSellerOrders() {
    try {
        const response = await getItemByIdWithToken(
            `${import.meta.env.VITE_APP_URL}/v2/sellers/${getUserId()}/orders`,
            getAccessToken(),
            0, 
            1
        )
        setSellerOrdersCount(response.totalElements)
    } catch (error) {
        console.log(error)
    }
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
        <div v-else class="hidden md:flex gap-6 font-medium items-center">
            <router-link :to="{ name: 'Cart' }">    
		        <div class="relative">
                    <p v-if="cart && cart.length > 0" class="absolute -top-1.5 -right-1.5 px-[0.35rem] rounded-full bg-[#D27B7B] text-[#F0EDEC] text-xs">{{ getCartItemCount() }}</p>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                        <path fill="#F0EDEC" d="M17 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2M1 2v2h2l3.6 7.59l-1.36 2.45c-.15.28-.24.61-.24.96a2 2 0 0 0 2 2h12v-2H7.42a.25.25 0 0 1-.25-.25q0-.075.03-.12L8.1 13h7.45c.75 0 1.41-.42 1.75-1.03l3.58-6.47c.07-.16.12-.33.12-.5a1 1 0 0 0-1-1H5.21l-.94-2M7 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2" />
                    </svg>
                </div>
            </router-link>
            <router-link v-if="getUserType() === 'SELLER'" :to="{ name: 'SaleOrderList' }">    
		        <div class="relative">
                    <p v-if="sellerOrdersCount > 0" class="itbms-bag-quantity absolute -top-1 -right-1.5 px-[0.35rem] rounded-full bg-[#D27B7B] text-[#F0EDEC] text-xs">{{ sellerOrdersCount }}</p>
                    <div class="itbms-bag-button bg-[#6F879C] rounded-md">
                        <svg xmlns="http://www.w3.org/2000/svg" width="27" height="27" viewBox="0 0 24 24">
                            <path fill="#F0EDEC" d="M4 18V7.1L2.45 3.75q-.175-.375-.025-.763t.525-.562t.763-.037t.562.512L6.2 7.05h11.6l1.925-4.15q.175-.375.563-.525t.762.05q.375.175.525.563t-.025.762L20 7.1V18q0 .825-.587 1.413T18 20H6q-.825 0-1.412-.587T4 18m6-5h4q.425 0 .713-.288T15 12t-.288-.712T14 11h-4q-.425 0-.712.288T9 12t.288.713T10 13" />
                        </svg>                    
                    </div>
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
