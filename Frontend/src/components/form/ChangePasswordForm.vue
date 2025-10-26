<script setup>
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import { ref, watch } from 'vue'
import { useRouter } from 'vue-router'
import { useUserStore } from '@/stores/UserStore'
import { postData } from '@/libs/fetchUtils'
import { computed } from 'vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'

const router = useRouter()
const userStore = useUserStore()
const { getAccessToken } = userStore
const isShowPopup = ref(false)
const isSuccess = ref(false)
const message = ref('')

const prop = defineProps({
  form : {
    type: String,
    default: 'change'
  },
  resetToken : String
})

const passwordForm = ref({
  oldPassword: '',
  newPassword: '',
  confirmPassword: ''
})

const invalid = ref({
  oldPassword: false,
  newPassword: false,
  confirmPassword: false
})

const handleDisabledButton = (field, value) => {
  invalid.value[field] = value
}

const invalidMessage = {
  oldPassword: 'Current password is required.',
  newPassword: 'Password is required, must be 8–14 characters long, and include uppercase, lowercase, number, and special character.',
  confirmPassword: 'Please confirm your new password.'
}

const validatePassword = (password) => {
  if (password.length < 8) return false

  const hasLower = /[a-z]/.test(password)
  const hasUpper = /[A-Z]/.test(password)
  const hasNumber = /[0-9]/.test(password)
  const hasSpecial = /[!@#$%^&*(),.?":{}|<>/_]/.test(password)

  return hasLower && hasUpper && hasNumber && hasSpecial
}

watch(passwordForm, (newVal) => {
  invalid.value.newPassword = !validatePassword(newVal.newPassword)
  invalid.value.confirmPassword =
    newVal.confirmPassword && newVal.newPassword !== newVal.confirmPassword
})

const disabled = computed(() => {
  const anyInvalid = Object.values(invalid.value).some(v => v === true)
  const hasEmpty = Object.entries(passwordForm.value).some(([key, value]) => {
    if (prop.form !== 'change' && key === 'oldPassword') return false;
    return value === '';
  });
  return anyInvalid || hasEmpty
})

const handleSubmit = async () => {
    isShowPopup.value = false
    if (passwordForm.value.newPassword !== passwordForm.value.confirmPassword) {
        isShowPopup.value = true
        isSuccess.value = false
        message.value = 'New password and confirm password do not match.'
        return
    }
    try {
        const response = prop.form === 'change' 
          ? await postData(`${import.meta.env.VITE_APP_URL}/v2/auth/change-password`, passwordForm.value, getAccessToken())
          : await postData(`${import.meta.env.VITE_APP_URL}/v2/users/reset-password`, { token : prop.resetToken, newPassword: passwordForm.value.newPassword, confirmPassword: passwordForm.value.confirmPassword });
        
        if (response.status === 204) {
            prop.form === 'change' 
              ? router.push({ name: 'Profile', query: { added: 'true' } }) 
              : router.push({ name: 'SignIn', query: { updated: 'true' } })
        } else {
            isShowPopup.value = true
            isSuccess.value = false
            message.value = `Failed to ${prop.form} password. Please try again.`
        }
    } catch (error) {
        isShowPopup.value = true
        message.value = 'There is a problem. Please try again later.'
    }
}

const cancel = () => {
  router.go(-1)
}
</script>

<template>
    <div class="font-rubik text-[#332A1E] bg-white w-full flex flex-col items-center justify-center h-screen px-10 md:px-22 lg:px-25">
        <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
        <div class="border border-gray-200 shadow-md rounded-lg py-10 w-full max-w-130 mx-auto px-7 md:px-10 space-y-5">
            <div class="flex flex-col items-center gap-2 mb-3">
                <p class="text-3xl font-semibold">{{form === 'change' ? 'Change Password' : 'Reset Password'}}</p>
            </div>
            <div class="space-y-4 py-3 pt-3">
                <FormInput v-if="form === 'change'" v-model="passwordForm.oldPassword" label="Old Password" :required="true" inputType="password" :maxlength="14" field="oldPassword"
                    placeholder="Enter current password" :invalidMessage="invalidMessage.oldPassword" @disabledButton="handleDisabledButton"/>
                <FormInput v-model="passwordForm.newPassword" label="New Password" :required="true" inputType="password" :min="8" :maxlength="14" field="newPassword"
                    placeholder="Enter new password" :invalidMessage="invalidMessage.newPassword" @disabledButton="handleDisabledButton"/>
                <FormInput v-model="passwordForm.confirmPassword" label="Confirm Password" :required="true" inputType="password" :min="8" :maxlength="14" field="confirmPassword"
                    placeholder="Enter confirm password" :invalidMessage="invalidMessage.confirmPassword" @disabledButton="handleDisabledButton"/>  
            </div>
            <div class="flex justify-center gap-4 mt-1">
                <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                <BaseButton @click="handleSubmit" text="Submit" bgColor="bg-[#4bbd80] border-transparent" textColor="text-white" class="itbms-save-button w-full" :disabled="disabled"/>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>
