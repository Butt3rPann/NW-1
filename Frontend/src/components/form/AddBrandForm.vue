<script setup>
import { useRouter } from 'vue-router'
import BrandForm from '@/components/form/BrandForm.vue'
import { addItem } from '@/libs/fetchUtils'

const router = useRouter()
const emit = defineEmits(['addSuccess'])

const handleNewBrand = async (newBrand) => {
    try {
        await addItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, newBrand)
        router.push({ name: 'BrandList', query: { added: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>
 
<template>
    <div class="flex flex-col items-center justify-center gap-7 pt-10 h-screen">
        <p class="itbms-add-button text-5xl font-bold font-rubik text-[#332A1E] ">Add Brand</p>
        <p class="font-medium text-lg">
            <router-link :to="{ name: 'SaleItemsList' }"><span class="itbms-item-list text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <span class="itbms-manage-brand text-[#332A1E]">Brands</span>
            <span class="text-[#332A1E]/50 mx-3"> > </span>
            <span class="itbms-manage-brand text-[#6F879C]">New Brand</span>
        </p>
        <BrandForm @submitAction="handleNewBrand" pathName="BrandList"/>
    </div>
</template>
 
<style scoped>

</style> 