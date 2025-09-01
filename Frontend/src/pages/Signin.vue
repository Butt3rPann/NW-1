<script setup>
import { ref , computed  } from 'vue'
import { useRouter } from 'vue-router'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import PopupMessage from '../components/elements/PopupMessage.vue'
import { postData } from '@/libs/fetchUtils'
import { useUserStore } from '@/stores/UserStore'
import { CookieUtils } from '@/libs/CookieUtils'

const userStore = useUserStore()
const { storeAccessToken } = userStore

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
  password: 'Password cannot be blank and must be at most 14 characters.'
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
        const loginUser = await postData(`${import.meta.env.VITE_APP_URL}/v2/users/authentications`, user.value)
        if (loginUser.access_token) {
            storeAccessToken(loginUser.access_token)
            CookieUtils.set('refresh_token', loginUser.refresh_token, { maxAge: 60*60*24 })
            router.push({ name: 'SaleItems' })
        } else if (loginUser.status === 401 || loginUser.status === 400) {
            isShowPopUp.value = true
            errorMessage.value = 'Email or Password is incorrect'
        } else if (loginUser.status === 403) {
            isShowPopUp.value = true
            errorMessage.value = "You need to activate your accout before signing in."
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
<div class="w-full font-rubik bg-white text-[#332A1E]"> 
    <div class="flex flex-col items-center justify-center h-screen pb-20 pt-30 px-10 md:px-22 lg:px-25"> 
        <PopupMessage :message="errorMessage" :isShowPopup="isShowPopUp" :isSuccess="false" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25" />
        <div class="bg-white border border-gray-200 shadow-md rounded-lg py-10 w-120 px-10 space-y-5">
            <p class="text-3xl font-semibold text-center">Log in</p>
            <div class="flex flex-col">
                <FormInput v-model="user.email" label="Email" :required="true" inputType="text" :maxlength="50" field="email" :trim="false" inputmode="email" pattern="^\\s*[a-zA-Z0-9._%+\\-]+@[a-zA-Z0-9.\\-]+\\.[a-zA-Z]{2,}\\s*$"
                    placeholder="Enter email" :limitLength="50"  className="itbms-email" :invalidMessage="invalidMessage.email" @disabledButton="handleDisabledButton"/>
                <FormInput v-model="user.password" label="Password" :required="true" inputType="password"  :maxlength="14" field="password" :trim="false"
                    placeholder="Enter password" :limitLength="14" className="itbms-password" :invalidMessage="invalidMessage.password" @disabledButton="handleDisabledButton"/>
            </div>
            <div class="flex justify-center gap-4 pt-2">
                <BaseButton @click="handleClick" text="Login" :disabled="disabled" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-submit-button w-full"/>
            </div>
            <p class="text-center text-sm sm:text-base lg:text-lg">
                Don't have an account? 
                <span class="underline cursor-pointer text-[#6F879C]" @click="router.push({ name: 'Registers' })">Sign up</span>
            </p>
        </div>
    </div>
</div>
</template>
 
<style scoped>

</style>
