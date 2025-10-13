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

watch(
  passwordForm,
  (newVal) => {
    invalid.value.newPassword = !validatePassword(newVal.newPassword)
    invalid.value.confirmPassword =
      newVal.confirmPassword && newVal.newPassword !== newVal.confirmPassword
  }
)

const disabled = computed(() => {
  const anyInvalid = Object.values(invalid.value).some(v => v === true)
  const hasEmpty = Object.values(passwordForm.value).some(v => v === '')
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
        const response = await postData(
        `${import.meta.env.VITE_APP_URL}/v2/auth/change-password`,passwordForm.value, getAccessToken())
        if (response.status === 204) {
            router.push({ name: 'Profile', query: { added: 'true' } })
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
    <div class="w-full min-h-screen font-rubik bg-white pt-10">
        <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
        <div class="flex flex-col items-center h-fit pb-15 pt-30 px-10 md:px-22 lg:px-25 ">
            <div class="bg-white border border-gray-200 shadow-md w-full max-w-140 flex h-fit rounded-lg overflow-hidden">
                <div class="w-full px-7 my-7 text-[#332A1E]">
                    <div class="flex flex-col items-center gap-2 mb-3">
                        <p class="text-3xl font-semibold">Change Password</p>
                    </div>
                    <div class="gap-4 py-3 pt-3">
                        <FormInput v-model="passwordForm.oldPassword" label="Current Password" :required="true" inputType="text" :min="8" :maxlength="14" field="oldPassword"
                            placeholder="Enter current password" :invalidMessage="invalidMessage.oldPassword" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="passwordForm.newPassword" label="New Password" :required="true" inputType="text" :min="8" :maxlength="14" field="newPassword"
                            placeholder="Enter new password" :invalidMessage="invalidMessage.newPasswordPassword" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="passwordForm.confirmPassword" label="Confirm Password" :required="true" inputType="text" :min="8" :maxlength="14" field="confirmPassword"
                            placeholder="Enter confirm password" :invalidMessage="invalidMessage.confirmPasswordPassword" @disabledButton="handleDisabledButton"/>  
                    </div>
                    <div class="flex justify-center gap-4 mt-1">
                        <BaseButton @click="handleSubmit" text="Submit" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-save-button w-full" :disabled="disabled"/>
                        <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                    </div>
                </div>
            </div>
        </div>
    </div>
</template>

<style scoped>

</style>
