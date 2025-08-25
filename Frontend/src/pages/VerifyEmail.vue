<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { addItem } from '@/libs/fetchUtils';

const route = useRoute();
const res = ref({})

onMounted(async () => {
    try {
        res.value = await addItem(`${import.meta.env.VITE_APP_URL}/v2/users/verify-email?jwtToken=${route.query.jwtToken}`)
        console.log(res.value);
    } catch (error) {
        console.log(error);
    }
    
})
</script>
 
<template>
    <div class="bg-white text-[#332A1E] font-rubik h-screen">
        <div class="px-7 md:px-13 lg:px-19 xl:px-26 pb-15 space-y-7 pt-22 md:pt-30">
            <p v-if="res.isActive">Your account has been successfully activated.</p>
            <p v-else>An error occured, or the verification link has expired. Please request a new verification email.</p>
        </div>
    </div>
</template>
 
<style scoped>

</style>