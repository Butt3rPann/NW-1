<script setup>
import { onMounted, ref } from 'vue';
import { useRoute } from 'vue-router';
import { patchItem } from '@/libs/fetchUtils';

const route = useRoute();
const res = ref('')

onMounted(async () => {
    try {
        res.value = await patchItem(`${import.meta.env.VITE_APP_URL}/v2/verify-email?token=${route.query.token}`)
    } catch (error) {
        console.log(error);
    }
    
})
</script>
 
<template>
    <div class="bg-white text-[#332A1E] font-rubik h-screen">
        <div class="px-7 md:px-13 lg:px-19 xl:px-26 pb-15 space-y-7 pt-22 md:pt-30">
            <p v-if="res === 200">Your account has been successfully activated.</p>
            <p v-else>An error occured, or the verification link has expired. Please request a new verification email.</p>
        </div>
    </div>
</template>
 
<style scoped>

</style>