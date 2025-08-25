<script setup>
import { ref, watchEffect } from 'vue'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import { useRouter } from 'vue-router'
import { uploadFormData } from '@/libs/fetchUtils'
import { previewBinaryFile } from '@/libs/utilities'
import PopupMessage from '../elements/PopupMessage.vue'

const router = useRouter()
const frontPreview = ref(null)
const backPreview = ref(null)

const user = ref({
  nickName: '',
  email: '',
  password: '',
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
    nickName: 'Nickname must be require.',
    email: 'Email must be require',
    password: 'Password must be at least 8 characters, including upper, lower, number, and special char',
    fullName: 'Fullname must be require at least 4 characters and no more than 40 characters.',
    phoneNumber: 'Mobile must be require',
    bankAccount: 'Bank Account Number must be require.',
    bankName: 'Bank name must be require.',
    idCardNumber: 'National Id must be require.',
    sellerNationalIdPhotos: 'National Id photos must be require.'
}
const isNull = ref({
    nickName: false,
    email: false,
    password: false,
    fullName: false,
    phoneNumber: false,
    bankAccount: false,
    bankName: false,
    idCardNumber: false,
    idCardImageFront: false,
    idCardImageBack: false
})

const handleDisabledButton = (field, value) => {
    isNull.value[field] = value
}

const disabled = ref(true)

const validatePassword = (password) => {
  if (password.length < 8) return false

  const hasLower = /[a-z]/.test(password)
  const hasUpper = /[A-Z]/.test(password)
  const hasNumber = /[0-9]/.test(password)
  const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password)

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
    const hasTwoFile = user.value.userType === 'SELLER'? (user.value.idCardImageFront !== null && user.value.idCardImageBack != null): true

    disabled.value = hasEmptyField || !isFullnameValid || !isPasswordValid || !hasTwoFile
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
            const value = user.value[key]
            if(value !== '' && value !== null) {
                formData.append(key, value)   
            }
        }

        const addedUser = await uploadFormData(`${import.meta.env.VITE_APP_URL}/v2/users/register`, formData)
        if (addedUser.status === 400 || addedUser.status === 500) {
            isShowPopUp.value = true
            throw new Error(addedUser.message || 'Save failed')
        }
        router.push({ name: 'SaleItems', query: { userAdded: 'true' } })
    } catch (error) {
        console.log(error)
  }
}
</script>
 
<template>
<div class="w-full font-rubik bg-white "> 
    <div class="flex flex-col items-center h-200 py-28 px-10 md:px-22 lg:px-25  "> 
        <PopupMessage message="Email already exists" :isShowPopup="isShowPopUp" :isSuccess="false" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25"/>
        <div v-show="user.userType === 'BUYER'">
            <div class="bg-[#F2EDEC] shadow-md w-220 flex h-150 rounded-lg px-3 pt-3">  
                <div class="bg-white h-143 w-80 rounded-lg">

                </div> 
                <div class="h-119 w-165 px-7 pt-3 ">
                    <p class="text-3xl font-semibold">Create your account</p>
                    <div class="itbms-account-type pt-2 flex space-x-20 text-[18px]">
                        <div>
                            <input type="radio" v-model="user.userType" value="BUYER">Buyer</input>
                        </div>
                        <div>
                            <input type="radio" v-model="user.userType" value="SELLER">Seller</input>
                        </div>
                    </div>
                    <div class="pt-3">
                        <FormInput v-model="user.nickName" label="Nickname" :required="true" inputType="text" :maxlength="30" field="nickName"
                            placeholder="Enter nickname" :invalidMessage="invalidMessage.nickName" className="itbms-nickname" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.email" label="Email" :required="true" inputType="text" :maxlength="30" field="email"
                            placeholder="Enter email" :invalidMessage="invalidMessage.email" className="itbms-email" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.password" label="Password" :required="true" inputType="password" :maxlength="25" field="password"
                            placeholder="Enter password" :invalidMessage="invalidMessage.password" className="itbms-password" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.fullName" label="Fullname" :required="true" inputType="text" :maxlength="40" field="fullName"
                            placeholder="Enter fullname" :invalidMessage="invalidMessage.fullName" className="itbms-fullname" @disabledButton="handleDisabledButton"/>   
                    </div>
                    <div class="flex justify-center gap-4 pt-2">
                        <BaseButton @click="handleClick" text="Submit" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-submit-button w-full" :disabled="disabled"/>
                        <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                    </div>
                </div>
            </div>
        </div>
        <div v-show="user.userType === 'SELLER'">
            <div class="bg-[#F2EDEC] shadow-md w-220 flex h-150 rounded-lg px-3 pt-3 grid-cols-2">  
                <div class=" px-7 pt-3 w-100">
                    <p class="text-3xl font-semibold">Create your account</p>
                    <div class="itbms-account-type pt-2 flex space-x-20 text-[18px]">
                        <div>
                            <input type="radio" v-model="user.userType" value="BUYER"> Buyer</input>
                        </div>
                        <div>
                            <input type="radio" v-model="user.userType" value="SELLER"> Seller</input>
                        </div>
                    </div>
                    <div class="pt-3">
                        <FormInput v-model="user.nickName" label="Nickname" :required="true" inputType="text" :maxlength="30" field="nickName"
                            placeholder="Enter nickname" :invalidMessage="invalidMessage.nickName" className="itbms-nickname" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.email" label="Email" :required="true" inputType="text" :maxlength="30" field="email"
                            placeholder="Enter email" :invalidMessage="invalidMessage.email" className="itbms-email" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.password" label="Password" :required="true" inputType="password" :maxlength="25" field="password"
                            placeholder="Enter password" :invalidMessage="invalidMessage.password" className="itbms-password" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="user.fullName" label="Fullname" :required="true" inputType="text" :maxlength="40" field="fullName"
                            placeholder="Enter fullname" :invalidMessage="invalidMessage.fullName" className="itbms-fullname" @disabledButton="handleDisabledButton"/> 
                    </div>
                    <div class="flex justify-center gap-4 pt-2">
                        <BaseButton @click="handleClick" text="Submit" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-submit-button w-full" :disabled="disabled"/>
                        <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                    </div>
                </div>
                <div class=" px-5 w-100">
                    <FormInput v-model="user.phoneNumber" label="Mobile Number" :required="true" inputType="text" :maxlength="40" field="phoneNumber"
                        placeholder="Enter mobile number" :invalidMessage="invalidMessage.phoneNumber" className="itbms-mobile" @disabledButton="handleDisabledButton"/>
                    <FormInput v-model="user.bankAccount" label="Bank Accout No" :required="true" inputType="text" :maxlength="40" field="bankAccount"
                        placeholder="Enter bank accout number" :invalidMessage="invalidMessage.bankAccount" className="itbms-bank-account-no" @disabledButton="handleDisabledButton"/>
                    <FormInput v-model="user.bankName" label="Bank Name" :required="true" inputType="text" :maxlength="40" field="bankName"
                        placeholder="Enter bank name" :invalidMessage="invalidMessage.bankName" className="itbms-bank-name" @disabledButton="handleDisabledButton"/>
                    <FormInput v-model="user.idCardNumber" label="National Card No" :required="true" inputType="text" :maxlength="40" field="idCardNumber"
                        placeholder="Enter national Id" :invalidMessage="invalidMessage.idCardNumber" className="itbms-card-no" @disabledButton="handleDisabledButton"/>
                    <div >
                        <p>National Card Photo</p>
                        <div class="flex gap-6">
                            <label class="flex items-center justify-center w-40 h-28 border rounded-2xl cursor-pointer hover:bg-gray-200">
                                <input type="file" accept=".jpg,.jpeg,.png" class="hidden itbms-card-photo-front" @change="handleFrontUpload" />
                                <span v-if="!frontPreview" class="text-gray-700">Front side</span>
                                <img v-if="frontPreview" :src="frontPreview" alt="Front preview" class="mt-2 w-full h-28 object-cover rounded"/>
                            </label>
                            <label class="flex items-center justify-center w-40 h-28 border rounded-2xl cursor-pointer hover:bg-gray-200">
                                <input type="file" accept=".jpg,.jpeg,.png" class="hidden itbms-card-photo-back" @change="handleBackUpload" />
                                <span v-if="!backPreview" class="text-gray-700">Back side</span>
                                <img v-if="backPreview" :src="backPreview" alt="Back preview" class="mt-2 w-full h-28 object-cover rounded"/>
                            </label>
                        </div>
                    </div>
                </div>
            </div>
        </div>
    </div> 
</div>
</template>
 
<style scoped>

</style>