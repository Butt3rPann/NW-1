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
const { getNickname, removeAccessToken, getUserId, getAccessToken, getCartItemCount, getUserType } = userStore
const { nickName, cart } = storeToRefs(userStore)

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

onMounted(async () => {
    try {
        const isLoggedIn = !!localStorage.getItem('access_token')
        if (isLoggedIn) {
            const data = await getItemByIdWithToken(`${import.meta.env.VITE_APP_URL}/v2/users/${getUserId()}/carts`, getAccessToken());
            userStore.setCart(data);
        }
    } catch (error) {
        console.log(error);
  }
})
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
                    <p v-if="cart && cart.length > 0" class="absolute -top-1.5 -right-1.5 px-[0.30rem] rounded-full bg-[#D27B7B] text-[#F0EDEC] text-xs">{{ getCartItemCount() }}</p>
                    <svg xmlns="http://www.w3.org/2000/svg" width="24" height="24" viewBox="0 0 24 24">
                        <path fill="#F0EDEC" d="M17 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2M1 2v2h2l3.6 7.59l-1.36 2.45c-.15.28-.24.61-.24.96a2 2 0 0 0 2 2h12v-2H7.42a.25.25 0 0 1-.25-.25q0-.075.03-.12L8.1 13h7.45c.75 0 1.41-.42 1.75-1.03l3.58-6.47c.07-.16.12-.33.12-.5a1 1 0 0 0-1-1H5.21l-.94-2M7 18c-1.11 0-2 .89-2 2a2 2 0 0 0 2 2a2 2 0 0 0 2-2a2 2 0 0 0-2-2" />
                    </svg>
                </div>
            </router-link>
            <router-link v-if="getUserType() === 'SELLER'" :to="{ name: 'SaleOrderList' }">    
		        <div class="relative">
                    <div class="itbms-bag-button bg-[#6F879C] w-10 p-2 rounded-md ml-4">
                        <svg viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier">
                            <path fill-rule="evenodd" clip-rule="evenodd" d="M16.5285 6C16.5098 5.9193 16.4904 5.83842 16.4701 5.75746C16.2061 4.70138 15.7904 3.55383 15.1125 2.65C14.4135 1.71802 13.3929 1 12 1C10.6071 1 9.58648 1.71802 8.88749 2.65C8.20962 3.55383 7.79387 4.70138 7.52985 5.75747C7.50961 5.83842 7.49016 5.9193 7.47145 6H5.8711C4.29171 6 2.98281 7.22455 2.87775 8.80044L2.14441 19.8004C2.02898 21.532 3.40238 23 5.13777 23H18.8622C20.5976 23 21.971 21.532 21.8556 19.8004L21.1222 8.80044C21.0172 7.22455 19.7083 6 18.1289 6H16.5285ZM8 11C8.57298 11 8.99806 10.5684 9.00001 9.99817C9.00016 9.97438 9.00044 9.9506 9.00084 9.92682C9.00172 9.87413 9.00351 9.79455 9.00718 9.69194C9.01451 9.48652 9.0293 9.18999 9.05905 8.83304C9.08015 8.57976 9.10858 8.29862 9.14674 8H14.8533C14.8914 8.29862 14.9198 8.57976 14.941 8.83305C14.9707 9.18999 14.9855 9.48652 14.9928 9.69194C14.9965 9.79455 14.9983 9.87413 14.9992 9.92682C14.9996 9.95134 14.9999 9.97587 15 10.0004C15 10.0004 15 11 16 11C17 11 17 9.99866 17 9.99866C16.9999 9.9636 16.9995 9.92854 16.9989 9.89349C16.9978 9.829 16.9957 9.7367 16.9915 9.62056C16.9833 9.38848 16.9668 9.06001 16.934 8.66695C16.917 8.46202 16.8953 8.23812 16.8679 8H18.1289C18.6554 8 19.0917 8.40818 19.1267 8.93348L19.86 19.9335C19.8985 20.5107 19.4407 21 18.8622 21H5.13777C4.55931 21 4.10151 20.5107 4.13998 19.9335L4.87332 8.93348C4.90834 8.40818 5.34464 8 5.8711 8H7.13208C7.10465 8.23812 7.08303 8.46202 7.06595 8.66696C7.0332 9.06001 7.01674 9.38848 7.00845 9.62056C7.0043 9.7367 7.00219 9.829 7.00112 9.89349C7.00054 9.92785 7.00011 9.96221 7 9.99658C6.99924 10.5672 7.42833 11 8 11ZM9.53352 6H14.4665C14.2353 5.15322 13.921 4.39466 13.5125 3.85C13.0865 3.28198 12.6071 3 12 3C11.3929 3 10.9135 3.28198 10.4875 3.85C10.079 4.39466 9.76472 5.15322 9.53352 6Z" fill="#ffffff"></path> </g>
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
