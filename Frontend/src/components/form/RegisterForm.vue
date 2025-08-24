<script setup>
import { ref , computed } from 'vue'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import { useRouter } from 'vue-router'
import { addItem } from '@/libs/fetchUtils'
import PopupMessage from '@/components/elements/PopupMessage.vue'

const router = useRouter()
const emit = defineEmits(['submitAction'])

const props = defineProps({
    userData: Object,
    pathName: String
})

const oldUser = ref(null)
const newUser = ref({
  nickname: props.userData?.nickname || '',
  fullname: props.userData?.fullname || '',
  email: props.userData?.email || '',
  password: props.userData?.password || '',
  mobileNumber: props.userData?.mobileNumber || '',
  bankAccountNumber: props.userData?.bankAccountNumber || '',
  bankName: props.userData?.bankName || '',
  nationalId: props.userData?.nationalId || '',
  sellerNationalIdPhotos: [],
  userType: props.userData?.userType || 'BUYER',  
  status: props.userData?.status || 'INACTIVE'
})

oldUser.value = {...newUser.value}

const invalidMessage = {
    nickname: 'Nickname must be require.',
    email: 'Email must be require',
    password: 'Password must be at least 8 characters, including upper, lower, number, and special char',
    fullname: 'Fullname must be require.',
    mobile: 'Mobile must be require',
    bankAccountNumber: 'Bank Account Number must be require.',
    bankName: 'Bank name must be require.',
    nationalId: 'National Id must be require.'
}
const invalid = ref({
    nickname: false,
    email: false,
    password: false,
    fullname: false,
    mobile: false,
    bankAccountNumber: false,
    bankName: false,
    nationalId: false
})

const handleDisabledButton = (field, value) => {
    invalid.value[field] = value
}

const disabled = computed(() => {
    const hasEmptyField = !newUser.value.nickname || !newUser.value.email || !newUser.value.password ||!newUser.value.fullname ||
                        (newUser.value.userType === 'SELLER' && (
                          !newUser.value.mobileNumber ||
                          !newUser.value.bankAccountNumber ||
                          !newUser.value.bankName ||
                          !newUser.value.nationalId
                        ))
    invalid.value.password = !validatePassword(newUser.value.password)
    const anyInvalid = Object.values(invalid.value).some(v => v === true)
    const unchanged = JSON.stringify(newUser.value) === JSON.stringify(oldUser.value)
    return hasEmptyField || unchanged || anyInvalid
})

const cancel = () => {
    router.push({ name: props.pathName })
}

const validatePassword = (password) => {
  if (password.length < 8) return false

  const hasLower = /[a-z]/.test(password)
  const hasUpper = /[A-Z]/.test(password)
  const hasNumber = /[0-9]/.test(password)
  const hasSpecial = /[!@#$%^&*(),.?":{}|<>]/.test(password)

  return hasLower && hasUpper && hasNumber && hasSpecial
}

const handleClick = async () => {
    const addedItem = { ...newUser.value }
    Object.keys(addedItem).forEach(key => {
        if (addedItem[key] === '') {
        addedItem[key] = null
        }
    })

    try {
        const addedUser = await addItem(`${import.meta.env.VITE_APP_URL}/v2/registers`, addedItem)
        console.log(addedUser);
        if (addedUser.status === 400 || addedUser.status === 500) {
        throw new Error(addedUser.message || 'Save failed')
        }
        router.push({ name: props.pathName, query: { added: 'true' } })

    } catch (error) {
        console.log(error)
        isSuccess.value = false
        message.value = 'The user could not be added.'
        isShowPopup.value = true
        setTimeout(() => isShowPopup.value = false, 1500)
  }
}
</script>
 
<template>
<div class="w-full font-rubik bg-white "> 
    <div class="flex flex-col items-center h-200 py-28 px-10 md:px-22 lg:px-25  "> 
        <div v-if="newUser.userType === 'BUYER'">
            <div class="bg-[#F2EDEC] shadow-md w-220 flex h-150 rounded-lg px-3 pt-3">  
                <div class="bg-white h-143 w-80 rounded-lg">

                </div> 
                <div class="h-119 w-165 px-7 pt-3 ">
                    <p class="text-3xl font-semibold">Create your account</p>
                    <div class="itbms-account-type pt-2 flex space-x-20 text-[18px]">
                        <div>
                            <input type="radio" v-model="newUser.userType" value="BUYER"> Buyer</input>
                        </div>
                        <div>
                            <input type="radio" v-model="newUser.userType" value="SELLER"> Seller</input>
                        </div>
                    </div>
                    <div class="pt-3">
                        <FormInput v-model="newUser.nickname" label="Nickname" :required="true" inputType="text" :maxlength="30" field="nickname"
                            placeholder="Enter nickname" :invalidMessage="invalidMessage.nickname" className="itbms-nickname" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="newUser.email" label="Email" :required="true" inputType="text" :maxlength="30" field="email"
                            placeholder="Enter email" :invalidMessage="invalidMessage.email" className="itbms-email" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="newUser.password" label="Password" :required="true" inputType="text" :maxlength="25" field="password"
                            placeholder="Enter password" :invalidMessage="invalidMessage.password" className="itbms-password" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="newUser.fullname" label="Fullname" :required="true" inputType="text" :maxlength="40" field="fullname"
                            placeholder="Enter fullname" :invalidMessage="invalidMessage.fullname" className="itbms-fullname" @disabledButton="handleDisabledButton"/>   
                    </div>
                    <div class="flex justify-center gap-4 pt-2">
                        <BaseButton @click="handleClick" text="Save" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-submit-button w-full" :disabled="disabled"/>
                        <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                    </div>
                </div>
            </div>
        </div>
        <div v-if="newUser.userType === 'SELLER'">
            <div class="bg-[#F2EDEC] shadow-md w-220 flex h-150 rounded-lg px-3 pt-3 grid-cols-2">  
                <div class=" px-7 pt-3 w-100">
                    <p class="text-3xl font-semibold">Create your account</p>
                    <div class="itbms-account-type pt-2 flex space-x-20 text-[18px]">
                        <div>
                            <input type="radio" v-model="newUser.userType" value="BUYER"> Buyer</input>
                        </div>
                        <div>
                            <input type="radio" v-model="newUser.userType" value="SELLER"> Seller</input>
                        </div>
                    </div>
                    <div class="pt-3">
                        <FormInput v-model="newUser.nickname" label="Nickname" :required="true" inputType="text" :maxlength="30" field="nickname"
                            placeholder="Enter nickname" :invalidMessage="invalidMessage.nickname" className="itbms-nickname" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="newUser.email" label="Email" :required="true" inputType="text" :maxlength="30" field="email"
                            placeholder="Enter email" :invalidMessage="invalidMessage.email" className="itbms-email" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="newUser.password" label="Password" :required="true" inputType="text" :maxlength="25" field="password"
                            placeholder="Enter password" :invalidMessage="invalidMessage.password" className="itbms-password" @disabledButton="handleDisabledButton"/>
                        <FormInput v-model="newUser.fullname" label="Fullname" :required="true" inputType="text" :maxlength="40" field="fullname"
                            placeholder="Enter fullname" :invalidMessage="invalidMessage.fullname" className="itbms-fullname" @disabledButton="handleDisabledButton"/> 
                    </div>
                    <div class="flex justify-center gap-4 pt-2">
                        <BaseButton @click="handleClick" text="Save" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-submit-button w-full" :disabled="disabled"/>
                        <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
                    </div>
                </div>
                <div class=" px-5 w-100">
                    <FormInput v-model="newUser.mobileNumber" label="Mobile" :required="true" inputType="text" :maxlength="40" field="mobileNumber"
                        placeholder="Enter mobile" :invalidMessage="invalidMessage.mobileNumber" className="itbms-mobile" @disabledButton="handleDisabledButton"/>
                    <FormInput v-model="newUser.bankAccountNumber" label="Bank Accout No" :required="true" inputType="text" :maxlength="40" field="bankAccountNumber"
                        placeholder="Enter bank accout number" :invalidMessage="invalidMessage.bankAccountNumber" className="itbms-bank-account-no" @disabledButton="handleDisabledButton"/>
                    <FormInput v-model="newUser.bankName" label="Bank Name" :required="true" inputType="text" :maxlength="40" field="bankName"
                        placeholder="Enter bank name" :invalidMessage="invalidMessage.bankName" className="itbms-bank-name" @disabledButton="handleDisabledButton"/>
                    <FormInput v-model="newUser.nationalId" label="National Card No" :required="true" inputType="text" :maxlength="40" field="nationalId"
                        placeholder="Enter national Id" :invalidMessage="invalidMessage.nationalId" className="itbms-card-no" @disabledButton="handleDisabledButton"/>
                    <div >
                        <p>National Card Photo</p>
                        <div class="flex gap-6">
                            <label class="flex items-center justify-center w-40 h-28 border rounded-2xl cursor-pointer hover:bg-gray-200">
                                <input type="file" accept=".jpg,.jpeg,.png" class="hidden itbms-card-photo-front" @change="handleFrontUpload" />
                                <span class="text-gray-700">Front side</span>
                            </label>
                            <label class="flex items-center justify-center w-40 h-28 border rounded-2xl cursor-pointer hover:bg-gray-200">
                                <input type="file" accept=".jpg,.jpeg,.png" class="hidden itbms-card-photo-back" @change="handleBackUpload" />
                                <span class="text-gray-700">Back side</span>
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