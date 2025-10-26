<script setup>
import { useRoute } from 'vue-router'
import { ref, onMounted } from 'vue'
import logoImg from '@/assets/images/logo.png'
import profileImg from '@/assets/images/profile.png'
import { useUserStore } from '@/stores/UserStore'
import { storeToRefs } from 'pinia'
import { getItemByIdWithToken, postData } from '@/libs/fetchUtils'
import router from '@/router'

const userStore = useUserStore()
const { getUserId, getAccessToken, getCartItemCount, getUserType, setSellerOrdersCount, isLoggedIn, removeAccessToken } = userStore
const { nickName, cart, sellerOrdersCount } = storeToRefs(userStore)

const route = useRoute()

const navItems = [
    { name: 'Home', pathname: 'Homepage' },
    { name: 'Products', pathname: 'SaleItems' },
    { name: 'Orders', pathname: 'OrderList' },
    { name: 'Contact', pathname: 'ContactUs' }
]

const isMenuOpen = ref(false)

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

const handleLogout = async () => {
    try {
    await postData(`${import.meta.env.VITE_APP_URL}/v2/auth/logout`)
    isMenuOpen.value = false
    removeAccessToken()
    router.push({ name: 'SaleItems' }) 
    } catch (error) {
        console.log(error);
    } 
}
</script>

<template>
    <div class="fixed top-0 w-full z-50 font-rubik bg-[#6F879C] text-[#F0EDEC] border-b border-[#8ea2b4] h-[4rem] md:h-[4.5rem] lg:h-[5rem] flex items-center px-5 md:px-7 lg:px-10 justify-between">
        <router-link to="/">
            <div class="flex items-center">
                <img :src="logoImg" alt="logo" class="w-7 md:w-10">
                <p class="font-bold text-lg md:text-lg lg:text-xl xl:text-[1.5rem]">ITB-MSHOP</p>
            </div>    
        </router-link>
        <div class="hidden md:flex md:items-center text-xs md:text-sm lg:text-base xl:text-lg font-medium md:gap-4 lg:gap-8 xl:gap-12">
            <router-link v-for="item in navItems" :key="item.name" :to="{ name: item.pathname }"
                class="p-1 transition-all duration-150" 
                :class="route.name === item.pathname
                    ? 'border-b-2'
                    : 'hover:border-b-2'">
                {{ item.name }}
            </router-link>
        </div>
        <div class="flex gap-5 font-medium items-center">
            <template v-if="isLoggedIn()">
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
            </template>
            <div v-else class="hidden md:flex gap-3 font-medium">
                <router-link to="/signin">
                    <p>Log in</p>
                </router-link>
                <p>|</p>
                <router-link to="/registers">
                    <p>Sign up</p>
                </router-link>
            </div>
            <div class="space-y-1 md:hidden py-2 px-1" @click="isMenuOpen = !isMenuOpen">
                <div class="menuIcon"></div>
                <div class="menuIcon"></div>
                <div class="menuIcon"></div>
            </div>
        </div>
        <div v-if="isMenuOpen" class="absolute left-0 top-14 bg-[#6F879C] border-t border-t-[#FAF6F5]/20 text-sm md:hidden font-medium flex items-center flex-col w-full gap-3 py-5">
            <router-link v-for="item in navItems" :key="item.name" :to="{ name: item.pathname }" @click="isMenuOpen = false" class="w-full flex justify-center transition-all duration-150">
                <span class="inline-block transition-all duration-150 p-1"  :class="{'border-b-2 border-white' : route.name === item.pathname}">
                    {{ item.name }}
                </span>
            </router-link>
            <div class="flex justify-center pt-2 items-center gap-3 font-medium" v-if="!isLoggedIn()">
                <router-link to="/signin" @click="isMenuOpen = false"  class="px-4 py-1 bg-white text-[#6F879C] rounded hover:bg-gray-200 transition-colors">
                    Log in
                </router-link>
                <router-link to="/registers" @click="isMenuOpen = false"  class="px-4 py-1 bg-white text-[#6F879C] rounded hover:bg-gray-200 transition-colors">
                    Sign up
                </router-link>
            </div>
            <div v-else class="flex flex-col gap-4">
                <router-link :to="{ name: 'Profile' }" @click="isMenuOpen = false" class="w-full flex justify-center transition-all duration-150">
                    <span class="inline-block transition-all duration-150 p-1"  :class="{'border-b-2 border-white' : route.name === 'Profile'}">
                        Profile
                    </span>
                </router-link>
                <router-link to="/registers" @click="handleLogout"  class="px-4 py-1 bg-white text-[#6F879C] rounded gap-1 hover:bg-gray-200 transition-colors flex">
                    <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" class="w-5 h-5 md:w-6 md:h-6">
                        <path fill="#6F879C" d="M569 337C578.4 327.6 578.4 312.4 569 303.1L425 159C418.1 152.1 407.8 150.1 398.8 153.8C389.8 157.5 384 166.3 384 176L384 256L272 256C245.5 256 224 277.5 224 304L224 336C224 362.5 245.5 384 272 384L384 384L384 464C384 473.7 389.8 482.5 398.8 486.2C407.8 489.9 418.1 487.9 425 481L569 337zM224 160C241.7 160 256 145.7 256 128C256 110.3 241.7 96 224 96L160 96C107 96 64 139 64 192L64 448C64 501 107 544 160 544L224 544C241.7 544 256 529.7 256 512C256 494.3 241.7 480 224 480L160 480C142.3 480 128 465.7 128 448L128 192C128 174.3 142.3 160 160 160L224 160z"/>
                    </svg>
                    Log out
                </router-link>
            </div>
        </div>
    </div>
    <div v-if="isMenuOpen" class="md:hidden fixed w-screen h-full bg-black opacity-35 z-40" @click="isMenuOpen = false"/>
</template>

<style scoped>
.menuIcon {
    width: 20px;
    height: 2px;
    background-color: #F0EDEC;
}
</style>
