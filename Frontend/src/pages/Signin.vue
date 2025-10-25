<script setup>
import { ref , computed  } from 'vue'
import { useRouter } from 'vue-router'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import PopupMessage from '../components/elements/PopupMessage.vue'
import { postData, getItemByIdWithToken } from '@/libs/fetchUtils'
import { useUserStore } from '@/stores/UserStore'
import { jwtDecode } from 'jwt-decode'

const userStore = useUserStore()
const { storeAccessToken, getUserType } = userStore

const router = useRouter()

const user = ref({
  email: '',
  password: ''
})

const invalid = ref({
    email: false,
    password: false
})

const isShowPopUp = ref(false)

const handleDisabledButton = (field, value) => {
    invalid.value[field] = value
}

const invalidMessage = {
  email: 'Please enter a valid email address.',
  password: 'Please enter your password.'
}

const disabled = computed(() => {
    const anyInvalid = Object.values(invalid.value).some(value => value === true)
    const hasEmptyField = (user.value.email === '' || user.value.password === '')

    return anyInvalid || hasEmptyField
})

const errorMessage = ref('')

const handleClick = async () => {
    isShowPopUp.value = false
    try { 
        const loginUser = await postData(`${import.meta.env.VITE_APP_URL}/v2/auth/login`, user.value)
        if (loginUser.access_token) {
            storeAccessToken(loginUser.access_token)
            const userId = jwtDecode(loginUser.access_token).id
            const cart = await getItemByIdWithToken(`${import.meta.env.VITE_APP_URL}/v2/users/${userId}/carts`, loginUser.access_token);
            userStore.setCart(cart);
	        router.push({ name: getUserType() === 'SELLER' ? 'SaleItemsList' : 'SaleItems' })
        } else if (loginUser.status === 401 || loginUser.status === 400) {
            isShowPopUp.value = true
            errorMessage.value = 'Email or Password is incorrect.'
        } else if (loginUser.status === 403) {
            isShowPopUp.value = true
            errorMessage.value = 'You need to activate your accout before signing in.'
        } else {
            isShowPopUp.value = true
            errorMessage.value = 'There is a problem. Please try again later.'
        }
    } catch (error) {
        console.log(error)
    }
}
</script>
 
<template>
<div class="font-rubik text-[#332A1E] bg-white w-full flex flex-col items-center justify-center h-screen px-10 md:px-22 lg:px-25"> 
    <PopupMessage :message="errorMessage" :isShowPopup="isShowPopUp" :isSuccess="false" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25" />
    <div class=" border border-gray-200 shadow-md rounded-lg py-10 w-full max-w-130 mx-auto px-7 md:px-10 space-y-5">
        <p class="text-2xl md:text-3xl font-semibold text-center">Log in</p>
        <div class="flex flex-col">
            <FormInput v-model="user.email" label="Email" :required="true" inputType="text" field="email" :trim="false" inputmode="email" pattern="^\s*[^\s@]+@[^\s@]+\.[^\s@]+\s*$"
                placeholder="Enter email" className="itbms-email" :invalidMessage="invalidMessage.email" @disabledButton="handleDisabledButton"/>
            <FormInput v-model="user.password" label="Password" :required="true" inputType="password" field="password" :trim="false" class="pt-3"
                placeholder="Enter password" className="itbms-password" :invalidMessage="invalidMessage.password" @disabledButton="handleDisabledButton"/>
            <p @click="router.push({ name : 'ForgetPassword'})" class="w-fit ml-auto text-sm text-[#6F879C] cursor-pointer">Forget Password?</p>
        </div>
        <div class="flex justify-center gap-4 pt-2">
            <BaseButton @click="handleClick" text="Login" :disabled="disabled" bgColor="bg-[#4bbd80] border-transparent" textColor="text-white" class="itbms-signin-button w-full"/>
        </div>
        <p class="text-center text-sm sm:text-base lg:text-lg">
            Don't have an account? 
            <span class="underline cursor-pointer text-[#6F879C]" @click="router.push({ name: 'Registers' })">Sign up</span>
        </p>
    </div>
</div>
</template>
 
<style scoped>

</style>
