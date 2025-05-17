<script setup>
import { editItem, getItemById} from '@/libs/fetchUtils'
import BrandForm from '@/components/form/BrandForm.vue'
import { useRoute, useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'
import ItemNotFound from '@/components/elements/ItemNotFound.vue'

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
    const editedItem = {...editedBrand}
    Object.keys(editedItem).forEach(key => {
        if (editedItem[key] === '') {
            editedItem[key] = null
        }
    })
    try {
        const edited = await editItem(`${import.meta.env.VITE_APP_URL}/v1/brands`, id, editedItem)
        if (edited.status === 400 || edited.status === 500) {
            throw new Error(edited.message)
        }
        router.push({ name: 'BrandList' , query: { edited: 'true' }})
    } catch (error) {
        console.log(error)
    }
}
</script>
 
<template>
    <div v-if="brand.id" class="flex flex-col items-center justify-center gap-7 pt-10 h-screen bg-white">
        <p class="text-5xl font-bold font-rubik text-[#332A1E] ">Edit Brand</p> 
        <BrandForm @submitAction="handleEditBrand" pathName="BrandList" :brandData="brand"/>
    </div>
    <ItemNotFound v-else title="Brand" description="The requested brand does not exist." backPathName="BrandList"/>
</template>
 
<style scoped></style>