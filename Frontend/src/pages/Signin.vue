<script setup>
import { ref , computed  } from 'vue'
import { useRouter } from 'vue-router'
import FormInput from '@/components/elements/FormInput.vue'
import BaseButton from '@/components/elements/BaseButton.vue'
import { uploadFormData } from '@/libs/fetchUtils'
import PopupMessage from '../components/elements/PopupMessage.vue'

const router = useRouter()

const user = ref({
  email: '',
  password: ''
})

const isNull = ref({
    email: false,
    password: false
})

const isShowPopUp = ref(false)

const handleDisabledButton = (field, value) => {
    isNull.value[field] = value
}

const isValidEmail = (email) => /^[^\s@]+@[^\s@]+\.[^\s@]+$/.test(email)

const disabled = computed(() => {
  return !(isValidEmail(user.value.email) && user.value.password !== '')
})

const handleClick = async () => {
  isShowPopUp.value = false
  try {
    const body = {}
    for (const key in user.value) {
      const value = user.value[key]
      if (value !== '' && value !== null) {
        body[key] = value
      }
    }

    const addedUser = await fetch(
        `${import.meta.env.VITE_APP_URL}/v2/users/authentications`,
        {
        method: "POST",
        headers: {
          "Content-Type": "application/json",
        },
        body: JSON.stringify(body),
        }
    ).then(res => res.json())

    if (addedUser.status === 401) {
        isShowPopUp.value = true
        throw new Error(addedUser.message || 'Email or Password is incorrect')
    }

    if (addedUser.status === 400) {
        isShowPopUp.value = true
        throw new Error(addedUser.message || 'Email or Password is incorrect')
    }

    else  {
        isShowPopUp.value = true
        throw new Error(addedUser.message || 'There is a problem. Please try again later.')
    }

    } catch (error) {
        console.log(error)
    }
}

const cancel = () => {
    router.push({ name: 'Homepage' })
}
</script>
 
<template>
<div class="w-full font-rubik bg-white ">
    <div class="flex flex-col items-center h-190">
        <PopupMessage message="Email or Password is incorrect":isShowPopup="isShowPopUp" :isSuccess="false" class="fixed top-10 left-1/2 transform -translate-x-1/2"/>
        <div class="bg-[#F2EDEC] shadow-md w-220 flex h-100 rounded-lg px-3 py-40">
            <div class="flex flex-col">
            <FormInput v-model="user.email" label="Email" :required="true" inputType="text" :maxlength="50" field="email"
                placeholder="Enter email"  className="itbms-email" @disabledButton="handleDisabledButton"/>
            <FormInput v-model="user.password" label="Password" :required="true" inputType="text"  :maxlength="14" field="password"
                placeholder="Enter password"  className="itbms-password" @disabledButton="handleDisabledButton"/>
            </div>
            <div class="flex justify-center gap-4 pt-2">
                <BaseButton @click="handleClick" text="Submit" :disabled="disabled" bgColor="bg-[#6F879C]" textColor="text-white" class="itbms-submit-button w-full"/>
                <BaseButton @click="cancel" text="Cancel" class="itbms-cancel-button w-full"/>
            </div>
        </div>
    </div>
</div>
</template>
 
<style scoped>

</style>