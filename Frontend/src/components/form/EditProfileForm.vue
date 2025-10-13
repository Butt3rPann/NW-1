<script setup>
import { ref, watchEffect, onMounted, computed } from 'vue'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import PopupMessage from '../elements/PopupMessage.vue'
import { useRouter  } from 'vue-router'
import { editItem, getItemById} from '@/libs/fetchUtils'
import { useUserStore } from '@/stores/UserStore'
import { maskNumber } from '@/libs/utilities'

const router = useRouter()
const message = ref('')
const isShowPopup = ref(false)
const isSuccess = ref(true)

const userStore = useUserStore()
const { getUserId, getAccessToken } = userStore
const oldUser = ref(null)

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
        oldUser.value = JSON.parse(JSON.stringify(user.value))
        maskedPhoneNumber.value = maskNumber(user.value.phoneNumber)
        maskedBankAccount.value = maskNumber(user.value.bankAccount)
    } catch (error) {
        console.log(error)
    }
})

const handleUpdateProfile = async () => {
    try {
        const editedUser = { ...user.value }
        const updated = await editItem(`${import.meta.env.VITE_APP_URL}/v2/users`, getUserId(), editedUser, getAccessToken())
        if (updated.status === 400 || updated.status === 500) throw new Error(updated.message)

        router.push({ name: 'Profile', query: { updated: 'true' } })

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
    const unchanged = JSON.stringify(user.value) === JSON.stringify(oldUser.value)

    disabled.value = hasEmptyField || !isFullnameValid || unchanged
})

const cancel = () => {
    router.push({ name: 'Profile' })
}
const goToChangePassword = () => {
    router.push({ name: 'ChangePassword' })
}
</script>
 
<template>
<div class="w-full min-h-screen font-rubik bg-white pt-10">
    <div class="flex flex-col items-center h-fit pb-15 pt-30 px-10 md:px-22 lg:px-25 ">
        <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
        <p class="font-medium text-sm md:text-base lg:text-lg mb-7">
            <router-link :to="{ name: 'SaleItems' }"><span class="itbms-home text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <span class="text-[#6F879C]">Profile</span>
        </p>    
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
                            placeholder="Enter email" :invalidMessage="invalidMessage.email" className="itbms-email" readonly/>
                        <FormInput v-model="user.fullName" label="Fullname" :required="true" inputType="text" :maxlength="60" field="fullName"
                            placeholder="Enter fullname" :invalidMessage="invalidMessage.fullName" className="itbms-fullname" @disabledButton="handleDisabledButton"/>  
                        <FormInput v-model="user.userType" label="Type" :required="true" inputType="text" field="userType"
                            placeholder="Enter fullname" :invalidMessage="invalidMessage.fullName" className="itbms-type" readonly/>  
                        <template v-if="user.userType == 'SELLER'">
                            <FormInput v-model="maskedPhoneNumber" label="Mobile Number" :required="true" inputType="text" :maxlength="20" field="phoneNumber"
                                placeholder="Enter mobile number" :invalidMessage="invalidMessage.phoneNumber" className="itbms-mobile" readonly/>
                            <FormInput v-model="maskedBankAccount" label="Bank Accout" :required="true" inputType="text" :maxlength="50" field="bankAccount"
                                placeholder="Enter bank accout number" :invalidMessage="invalidMessage.bankAccount" className="itbms-bankAccount" readonly/>
                            <FormInput v-model="user.bankName" label="Bank Name" :required="true" inputType="text" :maxlength="100" field="bankName"
                                placeholder="Enter bank name" :invalidMessage="invalidMessage.bankName" className="itbms-bankName" readonly/>
                        </template>
                    </div>
                    <div class="flex justify-center gap-4 mt-1">
                        <BaseButton @click="handleUpdateProfile" text="Submit" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-save-button w-full" :disabled="disabled"/>
                        <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                    </div>
                </div>
            </div>
        </div>
        <div @click="goToChangePassword" class="mt-8 flex items-center justify-between border border-gray-200 rounded-xl p-4 w-full max-w-180 hover:bg-gray-50 cursor-pointer transition">
            <div class="flex items-center space-x-3">
            <div class="w-10 h-10 flex items-center justify-center bg-gray-100 rounded-full">
                <svg fill="#000000" height="20px" width="20px" version="1.1" id="Layer_1" xmlns="http://www.w3.org/2000/svg" xmlns:xlink="http://www.w3.org/1999/xlink" viewBox="0 0 485.017 485.017" xml:space="preserve"><g id="SVGRepo_bgCarrier" stroke-width="0"></g><g id="SVGRepo_tracerCarrier" stroke-linecap="round" stroke-linejoin="round"></g><g id="SVGRepo_iconCarrier"> <g> <path d="M361.205,68.899c-14.663,0-28.447,5.71-38.816,16.078c-21.402,21.403-21.402,56.228,0,77.631 c10.368,10.368,24.153,16.078,38.815,16.078s28.447-5.71,38.816-16.078c21.402-21.403,21.402-56.228,0-77.631 C389.652,74.609,375.867,68.899,361.205,68.899z M378.807,141.394c-4.702,4.702-10.953,7.292-17.603,7.292 s-12.901-2.59-17.603-7.291c-9.706-9.706-9.706-25.499,0-35.205c4.702-4.702,10.953-7.291,17.603-7.291s12.9,2.589,17.603,7.291 C388.513,115.896,388.513,131.688,378.807,141.394z"></path> <path d="M441.961,43.036C414.21,15.284,377.311,0,338.064,0c-39.248,0-76.146,15.284-103.897,43.036 c-42.226,42.226-54.491,105.179-32.065,159.698L0.254,404.584l-0.165,80.268l144.562,0.165v-55.722h55.705l0-55.705h55.705v-64.492 l26.212-26.212c17.615,7.203,36.698,10.976,55.799,10.976c39.244,0,76.14-15.282,103.889-43.032 C499.25,193.541,499.25,100.325,441.961,43.036z M420.748,229.617c-22.083,22.083-51.445,34.245-82.676,34.245 c-18.133,0-36.237-4.265-52.353-12.333l-9.672-4.842l-49.986,49.985v46.918h-55.705l0,55.705h-55.705v55.688l-84.5-0.096 l0.078-37.85L238.311,208.95l-4.842-9.672c-22.572-45.087-13.767-99.351,21.911-135.029C277.466,42.163,306.83,30,338.064,30 c31.234,0,60.598,12.163,82.684,34.249C466.34,109.841,466.34,184.025,420.748,229.617z"></path> </g> </g></svg>
            </div>
            <div>
                <h3 class="font-semibold text-gray-800">Change Password</h3>
                <p class="text-sm text-gray-500">Update your password to keep your account secure</p>
            </div>
            </div>
            <svg xmlns="http://www.w3.org/2000/svg" fill="none" viewBox="0 0 24 24" stroke-width="1.5" stroke="currentColor" class="w-5 h-5 text-gray-400">
                <path stroke-linecap="round" stroke-linejoin="round" d="M9 5l7 7-7 7" />
            </svg>
        </div>
    </div>
</div>
</template>
 
<style scoped>

</style>
