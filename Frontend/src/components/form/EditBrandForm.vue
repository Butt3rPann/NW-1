<script setup>
import { editItem, getItemById} from '@/libs/fetchUtils'
import BrandForm from '@/components/form/BrandForm.vue'
import { useRoute, useRouter } from 'vue-router'
import { onMounted } from 'vue'
import { ref } from 'vue';

const router = useRouter()
const { params: { id } } = useRoute()
const brand = ref({})

onMounted(async () => {
    try {
        brand.value = await getItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, id)
    } catch (error) {
        console.log(error);
    }
})

const handleEditBrand = async (editedBrand) => {
    try {
        await editItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, id, editedBrand)
        router.push({ name: 'BrandList', params: { id }, query: { edited: 'true' } })
    } catch (error) {
        console.log(error)
    }
}
</script>
 
<template>
    <div class="flex flex-col items-center justify-center gap-7 pt-10 h-screen">
        <p class="text-5xl font-bold font-rubik text-[#332A1E] ">Edit Brand</p> 
        <BrandForm @submitAction="handleEditBrand" pathName="BrandList"/>
    </div>
</template>
 
<style scoped></style>