<script setup>
import { ref, watchEffect, onMounted, computed } from 'vue'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import PopupMessage from '../elements/PopupMessage.vue'
import { useRouter  } from 'vue-router'
import { editItem, getItemById} from '@/libs/fetchUtils'
import { useUserStore } from '@/stores/UserStore'

const router = useRouter()
const message = ref('')
const isShowPopup = ref(false)
const isSuccess = ref(true)

const userStore = useUserStore()
const { getUserId, getAccessToken } = userStore

const user = ref({
    email: '',
    fullName: '',
    phoneNumber: '',
    bankAccount: '',
    bankName: '',
    userType: 'BUYER',
    nickName: ''
})

const invalidMessage = {
    email: 'Email is required and must not exceed 100 characters.',
    password: 'Password must be required, max 14 characters, with uppercase, lowercase, number, and special character',
    fullName: 'Fullname must be required at least 4 characters and no more than 40 characters.',
    phoneNumber: 'Mobile must be required',
    bankAccount: 'Bank Account Number must be required.',
    bankName: 'Bank name must be required.',
    nickName: 'Nickname must be required.'
}

const isNull = ref({
    email: false,
    fullName: false,
    phoneNumber: false,
    bankAccount: false,
    bankName: false,
    nickName: false
})

const handleDisabledButton = (field, value) => {
    isNull.value[field] = value
}

const disabled = ref(true)

onMounted(async () => {
    try {
        user.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v2/users`, getUserId(), getAccessToken())
        maskedPhoneNumber.value = maskNumber(user.value.phoneNumber)
        maskedBankAccount.value = maskNumber(user.value.bankAccount)
    } catch (error) {
        console.log(error)
    }
})

const handleUpdateProfile = async () => {
    try {
        const editedUser = { ...user.value }

        console.log('EditedUser:', editedUser)

        const updated = await editItem(`${import.meta.env.VITE_APP_URL}/v2/users`, getUserId(), editedUser, getAccessToken())
        if (updated.status === 400 || updated.status === 500) throw new Error(updated.message)

        router.push({ name: 'Profile' })

    } catch (error) {
        console.log(error)
        message.value = 'Failed to update profile.'
        isSuccess.value = false
        isShowPopup.value = true
        setTimeout(() => isShowPopup.value = false, 1500)
    }
}

const maskedPhoneNumber = ref('')
const maskedBankAccount = ref('')

const maskNumber = (number) => {
  const numStr = String(number)
  const length = numStr.length
  let result = ''

  for (let i = 0; i < length; i++) {
    const isTargetDigit = i === length - 2 || i === length - 3 || i === length - 4;
    result += isTargetDigit ? numStr[i] : 'x'
  }

  return result
}

watchEffect(() => {
    for(const key in isNull.value) {
        const value = user.value[key]
        isNull.value[key] = !value
    }
    const requiredField = user.value.userType === 'SELLER'
            ? ['nickName', 'email', 'fullName', 'phoneNumber', 'bankAccount', 'bankName']
            : ['nickName', 'email', 'fullName']

    const hasEmptyField = requiredField.some(field => isNull.value[field] === true)
    const isFullnameValid = user.value.fullName && user.value.fullName.length >= 4

    disabled.value = hasEmptyField || !isFullnameValid
})

const cancel = () => {
    router.push({ name: 'Homepage' })
}

const isShowPopUp = ref(false)

</script>
 
<template>
<div class="w-full font-rubik bg-white ">
    <div class="flex flex-col items-center h-fit pb-15 pt-30 px-10 md:px-22 lg:px-25 ">
        <PopupMessage message="Profile data is updated successfully." :isShowPopup="isShowPopUp" :isSuccess="false" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
        <div class="bg-white border border-gray-200 shadow-md w-full max-w-180 flex h-fit rounded-lg overflow-hidden">
            <div class="bg-white border border-gray-200 shadow-md w-full max-w-180 flex h-fit rounded-lg overflow-hidden">  
                <div class="w-full px-7 my-7 text-[#332A1E]">
                    <div class="flex flex-col items-center gap-2 mb-3">
                        <p class="text-3xl font-semibold">Edit your profile</p>
                    </div>
                    <div class="pt-3" :class="{'grid grid-cols-2 gap-4 py-3' : user.userType == 'SELLER'}">
                        <FormInput v-model="user.nickName" label="Nickname" :required="true" inputType="text" :maxlength="40" field="nickName"
                            placeholder="Enter nickname" :invalidMessage="invalidMessage.nickName" className="itbms-nickname" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.email" label="Email" :required="true" inputType="text" :maxlength="50" field="email" inputmode="email" pattern="^[^\s@]+@[^\s@]+\.[^\s@]+$"
                            placeholder="Enter email" :invalidMessage="invalidMessage.email" className="itbms-email" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.fullName" label="Fullname" :required="true" inputType="text" :maxlength="60" field="fullName"
                            placeholder="Enter fullname" :invalidMessage="invalidMessage.fullName" className="itbms-fullname" @disabledButton="handleDisabledButton"/>   
                        <template v-if="user.userType == 'SELLER'">
                            <FormInput v-model="maskedPhoneNumber" label="Mobile Number" :required="true" inputType="text" :maxlength="20" field="phoneNumber"
                                placeholder="Enter mobile number" :invalidMessage="invalidMessage.phoneNumber" className="itbms-mobile" @disabledButton="handleDisabledButton"/>
                            <FormInput v-model="maskedBankAccount" label="Bank Accout" :required="true" inputType="text" :maxlength="50" field="bankAccount"
                                placeholder="Enter bank accout number" :invalidMessage="invalidMessage.bankAccount" className="itbms-bank-account-no" @disabledButton="handleDisabledButton"/>
                            <FormInput v-model="user.bankName" label="Bank Name" :required="true" inputType="text" :maxlength="100" field="bankName"
                                placeholder="Enter bank name" :invalidMessage="invalidMessage.bankName" className="itbms-bank-name" @disabledButton="handleDisabledButton"/>
                        </template>
                    </div>
                    <div class="flex justify-center gap-4 mt-1">
                        <BaseButton @click="handleUpdateProfile" text="Submit" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-submit-button w-full" :disabled="disabled"/>
                        <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                    </div>
                </div>
        </div>
    </div>
    </div>
</div>
</template>
 
<style scoped>

</style>