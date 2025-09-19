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

const userStore = useUserStore()
const user = ref({})
const id = ref(0)

onMounted(async () => {
  id.value = userStore.getUserId()
  
  if (id.value) {
    try {
      user.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/users`, id.value) 
    } catch (error) {
      console.log(error)
    }
  }
})

</script>
 
<template>
<div class="w-full h-screen font-rubik flex flex-col items-center justify-center text-[#332A1E] bg-white pb-20 pt-30 px-10 md:px-22 lg:px-25">
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
              <BaseButton :icon="editIcon" text="Edit Profile"/>
            </router-link>       
        </div>
        <div v-if="user.userType === 'BUYER'" class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6 mt-6">
            <div class="bg-white col-span-full w-full h-fit p-6 shadow-md rounded-3xl">
              <div class="flex items-center mb-2">
                <img :src="profileVector" class="w-5"/> 
                <p class="ml-2 font-semibold text-lg">Personal Information</p>
              </div>
              <div class="mt-8">
                <p class="text-sm text-gray-500">Nickname</p>
                <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.nickName }}</p>
              </div>
              <div class="mt-8">
                <p class="text-sm text-gray-500">Fullname</p>
                <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.fullName }}</p>
              </div>
              <div class="mt-8">
                <p class="text-sm text-gray-500">Email</p>
                <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.email }}</p>
              </div>
            </div>
        </div>
        <div v-if="user.userType === 'SELLER'" class="mt-6">
            <div class="bg-white p-8 shadow-md rounded-3xl">
                <div class="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-3 gap-6">
                    <div class="border-r-2 border-gray-200 pr-5">
                        <div class="flex items-center mb-2">
                            <img :src="mailIcon" class="w-5"/> 
                            <p class="ml-2 font-semibold text-lg">Contact</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Email</p>
                            <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.email }}</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Phone</p>
                            <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.phoneNumber }}</p>
                        </div>
                    </div>
                
                    <div class="border-r-2 border-gray-200 pr-5">
                        <div class="flex items-center mb-2">
                            <img :src="profileVector" class="w-4"/> 
                            <p class="ml-2 font-semibold text-lg">Personal Information</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Nickname</p>
                            <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.nickName }}</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Fullname</p>
                            <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.fullName }}</p>
                        </div>
                    </div>
                
                    <div class="p-2">
                        <div class="flex items-center mb-2">
                            <img :src="bankIcon" class="w-5"/> 
                            <p class="ml-2 font-semibold text-lg">Bank Information</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Bank name</p>
                            <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.bankName }}</p>
                        </div>
                        <div class="mt-8">
                            <p class="text-sm text-gray-500">Bank Account</p>
                            <p class="text-lg w-full bg-gray-50 p-2 pl-4 rounded-md overflow-auto no-scrollbar">{{ user.bankAccount }}</p>
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