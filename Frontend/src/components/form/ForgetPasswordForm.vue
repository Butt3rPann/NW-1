<script setup>
import { computed, ref } from 'vue';
import BaseButton from '../elements/BaseButton.vue';
import PopupMessage from '../elements/PopupMessage.vue';
import FormInput from '../elements/FormInput.vue';
import { postData } from '@/libs/fetchUtils';

const emailInvalid = ref(false)
const isShowPopUp = ref(false)

const handleDisabledButton = (field, value) => {
    emailInvalid.value = value
}

const email = ref('')
const message = ref('')
const invalidMessage = 'Please enter a valid email address.'

const disabled = computed(() => emailInvalid.value === true || email.value === '')

const handleClick = async () => {
    emailInvalid.value = true
    try {
        await postData(`${import.meta.env.VITE_APP_URL}/v2/users/forgot-password?email=${email.value}`)
        message.value = "A password reset link has been sent to your email."
        isShowPopUp.value = true
    } catch (error) {
        console.log(error);
    }
    
}
</script>
 
<template>
<div class="w-full font-rubik bg-white text-[#332A1E]"> 
    <div class="flex flex-col items-center justify-center h-screen pb-20 pt-30 px-10 md:px-22 lg:px-25"> 
        <PopupMessage :message="message" :isShowPopup="isShowPopUp" :isSuccess="true" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25" />
        <div class="bg-white border border-gray-200 shadow-md rounded-lg py-10 w-130 px-10 space-y-5">
            <div class="w-full flex items-center justify-center">
                <img src="../../assets/images/forgetPass.png" alt="forgetPasswordImg" class="w-48">
            </div>
            <div class="flex flex-col gap-3">
                <p class="text-3xl font-semibold text-center">Forget your password?</p>
                <p class="text-center text-sm text-gray-400">Enter your email so that we can send your password reset link</p>
            </div>
            <div class="flex flex-col">
                <FormInput v-model="email" label="Email" :required="true" inputType="text" field="email" :trim="false" inputmode="email" pattern="^\s*[^\s@]+@[^\s@]+\.[^\s@]+\s*$"
                    placeholder="Enter email" className="itbms-email" :invalidMessage="invalidMessage" @disabledButton="handleDisabledButton"/>
            </div>
            <div class="flex justify-center gap-4 pt-2">
                <BaseButton @click="handleClick" text="Send Email" :disabled="disabled" bgColor="bg-[#4bbd80] border-transparent" textColor="text-white" class="itbms-signin-button w-full"/>
            </div>
        </div>
    </div>
</div>
</template>
 
<style scoped>

</style>