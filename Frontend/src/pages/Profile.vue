<script setup>
import { useUserStore } from '@/stores/UserStore'
import { onMounted, ref } from 'vue'
import { getItemById } from '@/libs/fetchUtils.js'
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

</script>
 
<template>
<div class="w-full font-rubik flex flex-col items-center justify-center text-[#332A1E] bg-white pb-20 pt-30 px-10 md:px-22 lg:px-25">
    <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
    <div class="mt-5 mr-auto items-start justify-start">
        <p class="font-medium text-sm md:text-base lg:text-lg mb-7">
            <router-link :to="{ name: 'SaleItems' }"><span class="itbms-home text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <span class="text-[#6F879C]">Profile</span>
        </p>        
    </div>
    <p class="mb-3 text-3xl font-semibold"><span class="itbms-type">{{ type }} </span> Profile</p>
    <div class="w-3/4 h-fit p-10 shadow-md rounded-3xl bg-gray-100">
        <div class="flex items-center justify-between">
            <div class="flex items-center">
                <img :src="profileImg2" class="w-25"/>
                <div class="ml-3">
                    <p class="text-3xl font-bold mb-1">{{ user.nickName }}</p>
                    <p class="text-xl">{{ user.fullName }}</p>
                </div>            
            </div>     
            <router-link :to="{ name: 'EditProfile' }">
              <BaseButton :icon="editIcon" text="Edit Profile" class="itbms-profile-button"/>
            </router-link>      
        </div>
        <div v-if="user.userType === 'BUYER'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
            <div class="bg-white col-span-full w-full h-fit p-6 shadow-md rounded-3xl">
              <div class="flex items-center mb-2">
                <img :src="profileVector" class="w-5"/> 
                <p class="ml-2 font-semibold text-lg">Personal Information</p>
              </div>
              <div class="mt-6">
                <p class="text-sm text-gray-500">Nickname</p>
                <p class="itbms-nickname text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.nickName }}</p>
              </div>
              <div class="mt-6">
                <p class="text-sm text-gray-500">Fullname</p>
                <p class="itbms-fullname text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.fullName }}</p>
              </div>
              <div class="mt-6">
                <p class="text-sm text-gray-500">Email</p>
                <p class="itbms-email text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.email }}</p>
              </div>
            </div>
        </div>
        <div v-else-if="user.userType === 'SELLER'" class="mt-6">
            <div class="bg-white p-8 shadow-md rounded-3xl">
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <div class="border-r-2 border-gray-200 pr-5">
                        <div class="flex items-center mb-2">
                            <img :src="mailIcon" class="w-5"/> 
                            <p class="ml-2 font-semibold text-lg">Contact</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Email</p>
                            <p class="itbms-email text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.email }}</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Phone</p>
                            <p class="itbms-mobile text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ phone }}</p>
                        </div>
                    </div>
                
                    <div class="border-r-2 border-gray-200 pr-5">
                        <div class="flex items-center mb-2">
                            <img :src="profileVector" class="w-4"/> 
                            <p class="ml-2 font-semibold text-lg">Personal Information</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Nickname</p>
                            <p class="itbms-nickname text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.nickName }}</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Fullname</p>
                            <p class="itbms-fullname text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.fullName }}</p>
                        </div>
                    </div>
                
                    <div class="p-2">
                        <div class="flex items-center mb-2">
                            <img :src="bankIcon" class="w-5"/> 
                            <p class="ml-2 font-semibold text-lg">Bank Information</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Bank name</p>
                            <p class="itbms-bankName text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.bankName }}</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Bank Account</p>
                            <p class="itbms-bankAccount text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ bankNo }}</p>
                        </div>
                    </div>           
                </div>
            </div>
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
