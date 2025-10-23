<script setup>
import { ref, watchEffect, watch } from 'vue'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import { useRouter } from 'vue-router'
import { uploadFormData } from '@/libs/fetchUtils'
import { previewBinaryFile } from '@/libs/utilities'
import PopupMessage from '../elements/PopupMessage.vue'
import registerBuyer from '@/assets/images/registerBuyer.png'
import registerSeller from '@/assets/images/registerSeller.png'
import upload from '@/assets/images/upload.png'

const router = useRouter()
const frontPreview = ref(null)
const backPreview = ref(null)

const user = ref({
  nickName: '',
  email: '',
  password: '',
  confirmPassword: '',
  fullName: '',
  phoneNumber: '',
  bankAccount: '',
  bankName: '',
  idCardNumber: '',
  userType: 'BUYER',
  idCardImageFront: null,
  idCardImageBack: null
})

const invalidMessage = {
    nickName: 'Nickname must be required.',
    email: 'Email is required and must not exceed 100 characters.',
    password: 'Password is required, must be 8–14 characters long, and include uppercase, lowercase, number, and special character.',
    confirmPassword: 'Passwords do not match',
    fullName: 'Fullname must be required at least 4 characters and no more than 40 characters.',
    phoneNumber: 'Mobile must be required and must be a valid 10-digit number.',
    bankAccount: 'Bank Account Number must be required.',
    bankName: 'Bank name must be required.',
    idCardNumber: 'National Id must be required and must be a valid 13-digit number.',
    sellerNationalIdPhotos: 'National Id photos must be required.'
}

const isNull = ref({
    nickName: false,
    email: false,
    password: false,
    confirmPassword: false,
    fullName: false,
    phoneNumber: false,
    bankAccount: false,
    bankName: false,
    idCardNumber: false,
    idCardImageFront: false,
    idCardImageBack: false
})

const isCorrectConfirmPassword = ref(false)

const handleDisabledButton = (field, value) => {
    isNull.value[field] = value
}

watch(user, (newVal) => {
  isCorrectConfirmPassword.value =
    newVal.confirmPassword != '' && newVal.password !== newVal.confirmPassword
}, { deep: true })

const disabled = ref(true)

const validatePassword = (password) => {
  if (password.length < 8) return false

  const hasLower = /[a-z]/.test(password)
  const hasUpper = /[A-Z]/.test(password)
  const hasNumber = /[0-9]/.test(password)
  const hasSpecial = /[!@#$%^&*(),.?":{}|<>/_]/.test(password)

  return hasLower && hasUpper && hasNumber && hasSpecial
}

const handleFrontUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    user.value.idCardImageFront = file
    frontPreview.value = previewBinaryFile(file)
  }
}

const handleBackUpload = (event) => {
  const file = event.target.files[0]
  if (file) {
    user.value.idCardImageBack = file
    backPreview.value = previewBinaryFile(file)
  } 
}

watchEffect(() => {
    for(const key in isNull.value) {
        const value = user.value[key]
        isNull.value[key] = !value
    }
    const requiredField = user.value.userType === 'SELLER'
            ? ['nickName', 'email', 'password', 'fullName', 'phoneNumber', 'bankAccount', 'bankName', 'idCardNumber', 'sellerNationalIdPhotos']
            : ['nickName', 'email', 'password', 'fullName']

    const hasEmptyField = requiredField.some(field => isNull.value[field] === true)
    const isFullnameValid = user.value.fullName && user.value.fullName.length >= 4
    const isPasswordValid = validatePassword(user.value.password)
    const isConfirmPasswordMatch = user.value.confirmPassword === user.value.password
    const hasTwoFile = user.value.userType === 'SELLER'? (user.value.idCardImageFront !== null && user.value.idCardImageBack != null): true

    disabled.value = hasEmptyField || !isFullnameValid || !isPasswordValid || !isConfirmPasswordMatch || !hasTwoFile
})

const cancel = () => {
    router.push({ name: 'Homepage' })
}

const isShowPopUp = ref(false)

const handleClick = async () => {
    isShowPopUp.value = false
    try {
        const formData = new FormData()
        for(const key in user.value) {
            if(key === 'confirmPassword') continue
            const value = user.value[key]
            if(value !== '' && value !== null) {
                formData.append(key, value)   
            }
        }

        const addedUser = await uploadFormData(`${import.meta.env.VITE_APP_URL}/v2/auth/register`, formData)
        if (addedUser.status === 400 || addedUser.status === 500) {
            isShowPopUp.value = true
            throw new Error(addedUser.message || 'Save failed')
        }
        disabled.value = true
        router.push({ name: 'SaleItems', query: { userAdded: 'true' } })
    } catch (error) {
        console.log(error)
  }
}
</script>
 
<template>
<div class="w-full font-rubik bg-white flex flex-col items-center h-fit pb-15 pt-20 md:pt-30 px-10 md:px-22 lg:px-25"> 
        <PopupMessage message="Email already exists" :isShowPopup="isShowPopUp" :isSuccess="false" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
        <div class="bg-white border border-gray-200 shadow-md w-full max-w-300 flex h-fit rounded-lg overflow-hidden">  
            <div class="hidden flex w-5/12 lg:flex items-center justify-center" :class="user.userType == 'BUYER' ? 'bg-[#91B3FA]/60' : 'bg-[#C4C3F7]'">
                <img :src="user.userType == 'BUYER' ? registerBuyer : registerSeller" alt="registerImg" class="w-85">
            </div> 
            <div class="w-full lg:w-7/12 px-7 my-7 text-[#332A1E]">
                <div class="flex flex-col items-center gap-1 md:gap-2 mb-2 md:mb-3">
                    <p class="text-xl md:text-3xl font-semibold">Create your account</p>
                    <div class="itbms-account-type pt-2 flex space-x-20 text-[18px]">
                        <div class="flex space-x-4">
                            <label>
                            <input type="radio" v-model="user.userType" value="BUYER" class="hidden peer"/>
                            <div class="px-4 py-2 border text-[#6F879C] text-sm sm:text-base lg:text-lg border-[#6F879C] rounded-lg cursor-pointer transition peer-checked:bg-[#6F879C] peer-checked:text-white">Buyer</div>
                            </label>

                            <label>
                            <input type="radio" v-model="user.userType" value="SELLER" class="hidden peer"/>
                            <div class="px-4 py-2 border text-[#6F879C] text-sm sm:text-base lg:text-lg border-[#6F879C] rounded-lg cursor-pointer transition peer-checked:bg-[#6F879C] peer-checked:text-white">Seller</div>
                            </label>
                        </div>
                    </div>
                </div>
                <div class="pt-3" :class="{'grid grid-col md:grid-cols-2 gap-1 md:gap-4 py-4' : user.userType == 'SELLER'}">
                    <FormInput v-model="user.nickName" label="Nickname" :required="true" inputType="text" :maxlength="40" field="nickName"
                        placeholder="Enter nickname" :invalidMessage="invalidMessage.nickName" className="itbms-nickname" @disabledButton="handleDisabledButton"/>
                    <FormInput v-model="user.email" label="Email" :required="true" inputType="text" :maxlength="50" field="email" inputmode="email" pattern="^[^\s@]+@[^\s@]+\.[^\s@]+$"
                        placeholder="Enter email" :invalidMessage="invalidMessage.email" className="itbms-email" @disabledButton="handleDisabledButton"/>
                    <div class="grid grid-col md:grid-cols-2 md:gap-4 col-span-1 md:col-span-2">
                      <FormInput v-model="user.password" label="Password" :required="true" inputType="password" :maxlength="14" field="password" :min="8" 
                        placeholder="Enter password" :invalidMessage="invalidMessage.password" className="itbms-password" @disabledButton="handleDisabledButton"/>
                      <FormInput v-model="user.confirmPassword" label="Confirm Password" :required="true" inputType="password" :maxlength="14" field="confirmPassword"
                        placeholder="Confirm your password" :propsInvalid="isCorrectConfirmPassword" :invalidMessage="invalidMessage.confirmPassword" @disabledButton="handleDisabledButton"/>
                    </div>
                    <FormInput v-model="user.fullName" label="Fullname" :required="true" inputType="text" :maxlength="40" :min="4" field="fullName"
                        placeholder="Enter fullname" :invalidMessage="invalidMessage.fullName" className="itbms-fullname" @disabledButton="handleDisabledButton"/>   
                    <template v-if="user.userType == 'SELLER'">
                        <FormInput v-model="user.phoneNumber" label="Mobile Number" :required="true" inputType="text" :maxlength="10" field="phoneNumber" pattern="^\d{10}$"
                            placeholder="Enter mobile number" :invalidMessage="invalidMessage.phoneNumber" className="itbms-mobile" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.bankAccount" label="Bank Accout" :required="true" inputType="text" :maxlength="50" field="bankAccount"
                            placeholder="Enter bank accout number" :invalidMessage="invalidMessage.bankAccount" className="itbms-bank-account-no" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.bankName" label="Bank Name" :required="true" inputType="text" :maxlength="100" field="bankName"
                            placeholder="Enter bank name" :invalidMessage="invalidMessage.bankName" className="itbms-bank-name" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.idCardNumber" label="National Card" :required="true" inputType="text" :maxlength="13" field="idCardNumber" pattern="^\d{13}$"
                            placeholder="Enter national Id" :invalidMessage="invalidMessage.idCardNumber" className="itbms-card-no" @disabledButton="handleDisabledButton"/>
                    </template>
                </div>
                <template v-if="user.userType == 'SELLER'">
                    <p class="text-[#332A1E] font-medium text-sm sm:text-base lg:text-lg mb-2">National Card Photo<span class="text-red-700">*</span></p>
                    <div class="flex gap-6">
                        <label class="flex items-center justify-center w-40 h-28 border rounded-2xl cursor-pointer hover:bg-gray-200 border-[#6F879C] overflow-hidden">
                            <input type="file" accept=".jpg,.jpeg,.png" class="hidden itbms-card-photo-front" @change="handleFrontUpload" />
                            <div v-if="!frontPreview" class="flex flex-col justify-center items-center">
                                <img :src="upload" alt="upload" class="w-5">
                                <span class="text-sm md:text-md lg:text-lg text-gray-700">Front side</span>   
                            </div>
                            <img v-if="frontPreview" :src="frontPreview" alt="Front preview" class="w-full h-full object-cover rounded"/>
                        </label>
                        <label class="flex items-center justify-center w-40 h-28 border rounded-2xl cursor-pointer hover:bg-gray-200 border-[#6F879C] overflow-hidden">
                            <input type="file" accept=".jpg,.jpeg,.png" class="hidden itbms-card-photo-back" @change="handleBackUpload" />
                            <div v-if="!backPreview" class="flex flex-col justify-center items-center">
                                <img :src="upload" alt="upload" class="w-5">
                                <span class="text-sm md:text-md lg:text-lg text-gray-700">Back side</span>  
                            </div>                           
                            <img v-if="backPreview" :src="backPreview" alt="Back preview" class="w-full h-full object-cover rounded"/>
                        </label>
                    </div>
                </template>
                <div class="flex justify-center gap-4 mt-8">
                    <BaseButton @click="handleClick" text="Submit" bgColor="bg-[#4bbd80] border-transparent" textColor="text-white" class="itbms-submit-button w-full" :disabled="disabled"/>
                    <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                </div>
                <p class="text-center mt-5 text-sm sm:text-base lg:text-lg">
                    Already have an account?
                    <span class="underline cursor-pointer text-[#6F879C]" @click="router.push({ name: 'SignIn' })">Log in</span>
                </p>
            </div>
        </div> 
</div>
</template>
 
<style scoped>

</style>
