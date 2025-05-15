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
        <p class="text-5xl font-bold font-rubik text-[#332A1E] ">Add Brand</p> 
        <BrandForm @submitAction="handleNewBrand" pathName="BrandList"/>
    </div>
</template>
 
<style scoped>

</style> 