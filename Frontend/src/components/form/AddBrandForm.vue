<script setup>
import { useRouter } from 'vue-router'
import BrandForm from '@/components/form/BrandForm.vue'
import { addItem } from '@/libs/fetchUtils'
import { ref } from 'vue'
import PopupMessage from '../elements/PopupMessage.vue'

const router = useRouter()
const emit = defineEmits(['addSuccess'])
const message = ref('')
const isShowPopup = ref(false)
const isSuccess = ref(true)

const handleNewBrand = async (newBrand) => {
    try {
        const addedBrand = await addItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, newBrand)
        if (addedBrand.status === 400 || addedBrand.status === 500) {
            throw new Error(addedBrand.message)
        }
        isSuccess.value = true
        message.value = 'The brand has been added.'
        isShowPopup.value = true
        setTimeout(() => router.push({ name: 'BrandList' }), 1500)
    } catch (error) {
        console.log(error)
        isSuccess.value = false
        message.value = 'The brand could not be added.'
        isShowPopup.value = true
        setTimeout(() => isShowPopup.value = false, 2500)
    }
}

</script>
 
<template>
<div>
    <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mt-25" />
    <div class="flex flex-col items-center justify-center gap-7 pt-10 h-screen">
        <p class="itbms-add-button text-5xl font-bold font-rubik text-[#332A1E] ">Add Brand</p>
        <p class="font-medium text-lg">
            <router-link :to="{ name: 'SaleItemsList' }"><span class="itbms-item-list text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <router-link :to="{ name: 'BrandList' }"><span class="itbms-manage-brand text-[#332A1E] cursor-pointer">Brands</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <span class="itbms-manage-brand text-[#6F879C]">New Brand</span>
        </p>
        <BrandForm @submitAction="handleNewBrand" pathName="BrandList"/>
    </div>
</div>    
</template>
 
<style scoped>

</style> 