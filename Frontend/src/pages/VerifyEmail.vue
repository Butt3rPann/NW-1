<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { postData } from '@/libs/fetchUtils';

const route = useRoute();
const res = ref({})

onMounted(async () => {
    try {
        res.value = await postData(`${import.meta.env.VITE_APP_URL}/v2/auth/verify-email?jwtToken=${route.query.jwtToken}`)
    } catch (error) {
        console.log(error);
    }
    
})
</script>

<template>
    <div class="bg-white text-[#332A1E] font-rubik min-h-screen flex items-center justify-center">
      <div class="px-6 md:px-12 lg:px-16 xl:px-24 py-12 space-y-8 flex flex-col items-center text-center">
        
        <div>
          <svg v-if="res.isActive" class="w-24 h-24 text-green-600" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" clip-rule="evenodd" d="M12 21C16.9706 21 21 16.9706 21 12C21 7.02944 16.9706 3 12 3C7.02944 3 3 7.02944 3 12C3 16.9706 7.02944 21 12 21ZM16.7682 9.64018C17.1218 9.21591 17.0645 8.58534 16.6402 8.23178C16.2159 7.87821 15.5853 7.93554 15.2318 8.35982L11.6338 12.6774C11.2871 13.0934 11.0922 13.3238 10.9366 13.4653L10.9306 13.4707L10.9242 13.4659C10.7564 13.339 10.5415 13.1272 10.1585 12.7443L8.70711 11.2929C8.31658 10.9024 7.68342 10.9024 7.29289 11.2929C6.90237 11.6834 6.90237 12.3166 7.29289 12.7071L8.74428 14.1585L8.78511 14.1993L8.78512 14.1993C9.11161 14.526 9.4257 14.8402 9.71794 15.0611C10.0453 15.3087 10.474 15.5415 11.0234 15.5165C11.5728 15.4916 11.9787 15.221 12.2823 14.9448C12.5534 14.6983 12.8377 14.3569 13.1333 14.0021L13.1333 14.0021L13.1703 13.9577L16.7682 9.64018Z" fill="#54b15f"/>
          </svg>
          <svg v-else class="w-24 h-24 text-red-600" viewBox="0 0 24 24" fill="none" xmlns="http://www.w3.org/2000/svg">
            <path fill-rule="evenodd" clip-rule="evenodd" d="M21 12C21 16.9706 16.9706 21 12 21C7.02944 21 3 16.9706 3 12C3 7.02944 7.02944 3 12 3C16.9706 3 21 7.02944 21 12ZM15.7071 15.7071C15.3166 16.0976 14.6834 16.0976 14.2929 15.7071L12 13.4142L9.70711 15.7071C9.31658 16.0976 8.68342 16.0976 8.29289 15.7071C7.90237 15.3166 7.90237 14.6834 8.29289 14.2929L10.5858 12L8.29289 9.70711C7.90237 9.31658 7.90237 8.68342 8.29289 8.29289C8.68342 7.90237 9.31658 7.90237 9.70711 8.29289L12 10.5858L14.2929 8.29289C14.6834 7.90237 15.3166 7.90237 15.7071 8.29289C16.0976 8.68342 16.0976 9.31658 15.7071 9.70711L13.4142 12L15.7071 14.2929C16.0976 14.6834 16.0976 15.3166 15.7071 15.7071Z" fill="#DC2524"/>
          </svg>
        </div>
  
        <div class="space-y-4">
          <h2 class="text-2xl md:text-3xl font-semibold">
            Verification <span class="font-bold" :class="res.isActive ? 'text-[#54b15f]' : 'text-[#DC2524]'">{{ res.isActive ? 'Successful' : 'Failed' }}</span>
          </h2>
          <p class="text-gray-600 md:text-lg">
            <span v-if="res.isActive">Your account has been successfully activated.</span>
            <span v-else>An error occurred, or the verification link has expired.<br>Please request a new verification email.</span>
          </p>
        </div>
      </div>
    </div>
</template>
 
<style scoped>

</style>