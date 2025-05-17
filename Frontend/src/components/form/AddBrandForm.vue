<script setup>
import { useRouter } from 'vue-router'
import BrandForm from '@/components/form/BrandForm.vue'
import { addItem } from '@/libs/fetchUtils'

const router = useRouter()
const emit = defineEmits(['addSuccess'])

const handleNewBrand = async (newBrand) => {
    const addedItem = {...newBrand}
    Object.keys(addedItem).forEach(key => {
        if (addedItem[key] === '') {
            addedItem[key] = null
        }
    })
    try {
        const addedBrand = await addItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, addedItem)
        if (addedBrand.status === 400 || addedBrand.status === 500) {
            throw new Error(addedBrand.message)
        }
        router.push({ name: 'BrandList', query: { added: 'true' } })
    } catch (error) {
        console.log(error)
    }
}

</script>
 
<template>
<div class="bg-white">
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