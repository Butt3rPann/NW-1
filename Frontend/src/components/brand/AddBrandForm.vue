<script setup>
import { useRouter } from 'vue-router'
import BrandForm from '@/components/form/BrandForm.vue'
import { postData } from '@/libs/fetchUtils'
import { ref } from 'vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'

const router = useRouter()
const message = ref('')
const isShowPopup = ref(false)
const isSuccess = ref(true)

const handleNewBrand = async (newBrand) => {
    const addedItem = {...newBrand}
    Object.keys(addedItem).forEach(key => {
        if (addedItem[key] === '') {
            addedItem[key] = null
        }
    })
    try {
        const addedBrand = await postData(`${import.meta.env.VITE_APP_URL}/v1/brands`, addedItem)
        if (addedBrand.status === 400 || addedBrand.status === 500) {
            throw new Error(addedBrand.message)
        }
        router.push({ name: 'BrandList', query: { added: 'true' } })
    } catch (error) {
        console.log(error)
        isSuccess.value = false
        message.value = 'The brand could not be added.'
        isShowPopup.value = true
        setTimeout(() => isShowPopup.value = false, 1500)
    }
}

</script>
 
<template>
<div class="bg-white font-rubik">
    <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mx-3 md:mx-0 mt-18 md:mt-22 lg:mt-25" />
    <div class="flex flex-col items-center justify-center gap-4 lg:gap-8 px-10 md:px-22 lg:px-25 pt-22 md:pt-30 pb-13 lg:pb-17">
        <p class="itbms-add-button text-2xl md:text-4xl lg:text-5xl xl:text-6xl font-bold font-rubik text-[#332A1E] text-center leading-tight">Add Brand</p>
        <p class="font-medium text-sm sm:text-base lg:text-lg">
            <router-link :to="{ name: 'SaleItemsList' }"><span class="itbms-item-list text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-1.5 md:mx-3"> > </span>
            <router-link :to="{ name: 'BrandList' }"><span class="itbms-manage-brand text-[#332A1E] cursor-pointer">Brands</span></router-link>
            <span class="text-[#332A1E]/50 mx-1.5 md:mx-3"> > </span>
            <span class="itbms-manage-brand text-[#6F879C]">New Brand</span>
        </p>
        <BrandForm @submitAction="handleNewBrand" pathName="BrandList" class="max-w-150"/>
    </div>
</div>    
</template>
 
<style scoped>

</style> 
