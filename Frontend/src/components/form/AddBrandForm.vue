<script setup>
import { useRouter } from 'vue-router'
import BrandForm from '@/components/form/BrandForm.vue'
import { addItem } from '@/libs/fetchUtils'

// const router = useRouter()

const emit = defineEmits(['addSuccess'])

const handleNewBrand = async (newBrand) => {
    console.log('Hi2');
    
    try {
        await addItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, newBrand)
        console.log('success');
        
        // router.push({ name: 'Brands', query: { added: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>
 
<template>
    <div class="flex flex-col items-center justify-center gap-7 pt-10 h-screen">
        <p class="text-5xl font-bold font-rubik text-[#332A1E] ">Add Brand</p> 
        <BrandForm @submitAction="handleNewBrand" path="/brands"/>
    </div>
</template>
 
<style scoped>

</style> 