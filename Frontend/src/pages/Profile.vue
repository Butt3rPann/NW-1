<script setup>
import { useUserStore } from '@/stores/UserStore'
import { onMounted, ref } from 'vue'
import { getItemById, postData } from '@/libs/fetchUtils.js'
import profileImg2 from '@/assets/images/profileImg2.png'
import profileVector from '@/assets/images/profileVector.png'
import mailIcon from '@/assets/images/mailIcon.png'
import bankIcon from '@/assets/images/bankIcon.png'
import BaseButton from '@/components/elements/BaseButton.vue'
import editIcon from "@/assets/images/edit.png"
import { useRoute } from 'vue-router'
import PopupMessage from '@/components/elements/PopupMessage.vue'
import { maskNumber } from '@/libs/utilities'
import router from '@/router'

const userStore = useUserStore()
const user = ref({})
const id = ref(0)
const route = useRoute()
const type = ref('')
const phone = ref('')
const bankNo = ref('')
const { removeAccessToken } = userStore

const message = ref('')
const isShowPopup = ref(false)
const isSuccess = ref(false)

onMounted(async () => {
  id.value = userStore.getUserId()
  
  if (id.value) {
    try {
      user.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/users`, id.value, userStore.getAccessToken())
      type.value = user.value.userType[0] + user.value.userType.slice(1).toLowerCase()
      phone.value = maskNumber(user.value.phoneNumber)
      bankNo.value = maskNumber(user.value.bankAccount)
    } catch (error) {
      console.log(error)
    }
  }
})

if (route.query.updated === 'true') {
  message.value = 'Profile data is updated successfully.'
  router.replace({ query: { } })
  isSuccess.value = true
  isShowPopup.value = true
  setTimeout(() => isShowPopup.value = false , 1500)
}

if (route.query.added === 'true') {
  message.value = 'Password changed successfully.'
  router.replace({ query: { } })
  isSuccess.value = true
  isShowPopup.value = true
  setTimeout(() => isShowPopup.value = false , 1500)
}

const handleLogout = async () => {
  try {
    await postData(`${import.meta.env.VITE_APP_URL}/v2/auth/logout`)
    removeAccessToken()
    router.push({ name: 'SaleItems' })  
  } catch (error) {
    console.log(error);
  }
}
</script>
 
<template>
<div class="w-full font-rubik flex flex-col items-center justify-center text-[#332A1E] bg-white pb-20 pt-30">
    <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
    <p class="mb-5 text-2xl lg:text-3xl font-semibold"><span class="itbms-type">{{ type }} </span> Profile</p>
    <div class="w-[90%] md:w-7/8 lg:w-3/4 h-fit p-4 md:p-8 lg:p-10 shadow-md rounded-3xl bg-gray-100">
        <div class="flex items-center justify-between">
            <div class="flex items-center">
                <img :src="profileImg2" class="w-15 md:w-17 lg:w-20"/>
                <div class="ml-1 md:ml-2 lg:ml-3">
                    <p class="text-xl md:text-2xl lg:text-3xl font-bold mb-1 break-all">{{ user.nickName }}</p>
                    <p class="text-sm md:text-md lg:text-xl break-all">{{ user.fullName }}</p>
                </div>            
            </div>     
            <router-link :to="{ name: 'EditProfile' }" class="ml-2 flex-shrink-0">
              <BaseButton :icon="editIcon" text="Edit" class="itbms-profile-button"/>
            </router-link>     
        </div>
        <div v-if="user.userType === 'BUYER'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
            <div class="bg-white col-span-full w-full h-fit p-6 shadow-md rounded-3xl">
              <div class="flex items-center mb-2">
                <img :src="profileVector" class="w-5"/> 
                <p class="ml-2 font-semibold md:text-md lg:text-lg">Personal Information</p>
              </div>
              <div class="mt-4 lg:mt-8">
                <p class="text-sm text-gray-500">Nickname</p>
                <p class="itbms-nickname text-sm md:text-md lg:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.nickName }}</p>
              </div>
              <div class="mt-4 lg:mt-8">
                <p class="text-sm text-gray-500">Fullname</p>
                <p class="itbms-fullname text-sm md:text-md lg:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.fullName }}</p>
              </div>
              <div class="mt-4 lg:mt-8">
                <p class="text-sm text-gray-500">Email</p>
                <p class="itbms-email text-sm md:text-md lg:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.email }}</p>
              </div>
            </div>
        </div>
        <div v-else-if="user.userType === 'SELLER'" class="mt-6">
            <div class="bg-white p-6 shadow-md rounded-3xl">
                <div class="grid grid-cols-1 lg:grid-cols-3 gap-2 lg:gap-4">
                    <div class="lg:border-r-2 border-gray-200 pb-5 md:p-3">
                        <div class="flex items-center mb-2">
                            <img :src="mailIcon" class="w-5"/> 
                            <p class="ml-2 font-semibold text-lg">Contact</p>
                        </div>
                        <div class="mt-4 lg:mt-8">
                            <p class="text-sm text-gray-500">Email</p>
                            <p class="itbms-email text-sm md:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.email }}</p>
                        </div>
                        <div class="mt-4 lg:mt-8">
                            <p class="text-sm text-gray-500">Phone</p>
                            <p class="itbms-mobile text-sm md:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ phone }}</p>
                        </div>
                    </div>
                
                    <div class="border-t lg:border-t-0 lg:border-r-2 border-gray-200 py-5 md:p-3">
                        <div class="flex items-center mb-2">
                            <img :src="profileVector" class="w-4"/> 
                            <p class="ml-2 font-semibold text-lg">Personal Information</p>
                        </div>
                        <div class="mt-4 lg:mt-8">
                            <p class="text-sm text-gray-500">Nickname</p>
                            <p class="itbms-nickname text-sm md:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.nickName }}</p>
                        </div>
                        <div class="mt-4 lg:mt-8">
                            <p class="text-sm text-gray-500">Fullname</p>
                            <p class="itbms-fullname text-sm md:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.fullName }}</p>
                        </div>
                    </div>
                
                    <div class="border-t lg:border-t-0 border-gray-200 pt-5 md:p-3">
                        <div class="flex items-center mb-2">
                            <img :src="bankIcon" class="w-5"/> 
                            <p class="ml-2 font-semibold text-lg">Bank Information</p>
                        </div>
                        <div class="mt-4 lg:mt-8">
                            <p class="text-sm text-gray-500">Bank name</p>
                            <p class="itbms-bankName text-sm md:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.bankName }}</p>
                        </div>
                        <div class="mt-4 lg:mt-8">
                            <p class="text-sm text-gray-500">Bank Account</p>
                            <p class="itbms-bankAccount text-sm md:text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ bankNo }}</p>
                        </div>
                    </div>           
                </div>
            </div>
        </div>
        <div @click="handleLogout" class="flex gap-2 shadow-md text-[#C43737] py-3 px-4 rounded-2xl bg-[#FFFFFF] mt-4 cursor-pointer itbms-logout-button">
            <svg xmlns="http://www.w3.org/2000/svg" viewBox="0 0 640 640" class="w-5 h-5 md:w-6 md:h-6">
              <path fill="#C43737" d="M569 337C578.4 327.6 578.4 312.4 569 303.1L425 159C418.1 152.1 407.8 150.1 398.8 153.8C389.8 157.5 384 166.3 384 176L384 256L272 256C245.5 256 224 277.5 224 304L224 336C224 362.5 245.5 384 272 384L384 384L384 464C384 473.7 389.8 482.5 398.8 486.2C407.8 489.9 418.1 487.9 425 481L569 337zM224 160C241.7 160 256 145.7 256 128C256 110.3 241.7 96 224 96L160 96C107 96 64 139 64 192L64 448C64 501 107 544 160 544L224 544C241.7 544 256 529.7 256 512C256 494.3 241.7 480 224 480L160 480C142.3 480 128 465.7 128 448L128 192C128 174.3 142.3 160 160 160L224 160z"/></svg>
            <span class="text-sm md:text-md lg:text-lg font-medium">Logout</span>
        </div>
    </div>
</div>
</template>
 
<style scoped>
.no-scrollbar::-webkit-scrollbar {
  display: none; 
}
.no-scrollbar {
  -ms-overflow-style: none; 
  scrollbar-width: none;   
}
</style>
