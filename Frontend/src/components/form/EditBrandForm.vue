<script setup>
import { editItem, getItemById} from '@/libs/fetchUtils'
import BrandForm from '@/components/form/BrandForm.vue'
import { useRoute, useRouter } from 'vue-router'
import { onMounted, ref } from 'vue'
import PopupMessage from '@/components/elements/PopupMessage.vue'
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

const message = ref('')
const isShowPopup = ref(false)
const isSuccess = ref(true)

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
        isSuccess.value = true
        message.value = 'The brand has been updated.'
        isShowPopup.value = true
        setTimeout(() => router.push({ name: 'BrandList' }), 800);
    } catch (error) {
        console.log(error)
        isSuccess.value = false
        message.value = 'The brand could not be updated.'
        isShowPopup.value = true
        setTimeout(() => isShowPopup.value = false, 1500)
    }
}
</script>
 
<template>
    <div v-if="brand.id" class="flex flex-col items-center justify-center gap-7 pt-10 h-screen bg-white">
        <PopupMessage :isSuccess="isSuccess" :message="message" :isShowPopup="isShowPopup" class="fixed mt-25" />
        <p class="text-5xl font-bold font-rubik text-[#332A1E] ">Edit Brand</p> 
        <BrandForm @submitAction="handleEditBrand" pathName="BrandList" :brandData="brand"/>
    </div>
    <ItemNotFound v-else title="Brand" description="The requested brand does not exist." backPathName="BrandList"/>
</template>
 
<style scoped></style>