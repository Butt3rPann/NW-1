<script setup>
import { onMounted, ref } from 'vue'
import { getItems, deleteItemById } from '@/libs/fetchUtils'
import ListModel from '@/components/model/ListModel.vue';
import DeleteConfirmation from '@/components/elements/DeleteConfirmation.vue';
import PopupMessage from '@/components/elements/PopupMessage.vue';
import addIcon from '@/assets/images/add.png'
import BaseButton from '@/components/elements/BaseButton.vue';
import { useRoute } from 'vue-router';
import router from '@/router';

const brands = ref([])

onMounted(async () => {
    try {
        brands.value = await getItems(`${import.meta.env.VITE_APP_URL}/v1/brands`)
    } catch (error) {
        console.log(error);
    }
})

const isShowPopup = ref(false)
const message = ref('')
const deletedId = ref(null)
const route = useRoute()

if (route.query.added === 'true') {
    router.replace({query: { }})
    message.value = 'The brand has been added'
    isShowPopup.value = true
}

function deleteBrandById(id) {
    showDelConfirm.value = true
    deletedId.value = id
}

const showDelConfirm = ref(false)

function closeDelConfirm() {
    showDelConfirm.value = false
    deletedId.value = null
}

async function deleteBrand() {
    try {
        const status = await deleteItemById(`${import.meta.env.VITE_APP_URL}/v1/brands`, deletedId.value)
        if (status === 404) {
            return
        } else {
            brands.value = brands.value.filter(item => item.id !== deletedId.value)
            message.value = "The brand has been deleted."
            showDelConfirm.value = false
            isShowPopup.value = true
        }
    } catch (error) {
        console.log(error);
    }
}
</script>

<template>
    <div>
        <PopupMessage :message="message" :isShowPopup="isShowPopup" class="fixed mt-25" />
        <div class="font-rubik mx-35 mb-15 mt-30">
            <div class="flex justify-between items-center mb-4">
                <p class="text-[3.5rem] font-bold text-[#332A1E]">Brands</p>
                <router-link :to="{ name: 'AddBrand' }">
                    <BaseButton :icon="addIcon" text="Add Brand" textColor="text-[#F2EDEC]" bgColor="bg-[#6F879C]" class="itbms-add-button"/>
                </router-link>
            </div>
            <p class="font-medium text-lg mb-7">
                <router-link :to="{ name: 'SaleItemsList' }"><span class="itbms-item-list text-[#332A1E] cursor-pointer">All Sale Items</span></router-link>
                <span class="text-[#332A1E]/50 mx-3"> > </span>
                <span class="text-[#6F879C]">Brands</span>
            </p>
            <div class="border border-[#CFC8BE] rounded-md overflow-hidden bg-white">
                <ListModel :items="brands" view="list">
                    <template #header>
                        <p class="w-[25%]">id</p>
                        <p class="w-[75%]">Name</p>
                        <p class="w-[25%]">Action</p>
                    </template>
                    <template #item="slotProps">
                        <p class="w-[25%]">{{ slotProps.itemInList.id }}</p>
                        <p class="w-[75%]">{{ slotProps.itemInList.name }}</p>
                        <div class="w-[25%] flex justify-center gap-3">
                            <router-link :to="{ name: 'EditBrand', params: { id: slotProps.itemInList.id } }"
                                class="itbms-edit-button border-2 border-[#6F879C] text-[#6F879C] py-1 px-2.5 hover:bg-[#6F879C] hover:text-[#F2EDEC]">
                                E
                            </router-link>
                            <p @click="deleteBrandById(slotProps.itemInList.id)"
                                class="itbms-delete-button border-2 border-[#D27B7B] text-[#D27B7B] cursor-pointer py-1 px-2.5 hover:bg-[#D27B7B] hover:text-[#F2EDEC]">
                                D
                            </p>
                        </div>
                    </template>
                </ListModel>
                <div v-if="!brands.length" class="flex flex-col items-center space-y-3 py-18">
                    <img src="../assets/images/emptySaleItems.png" alt="EmptySaleItems" class="w-20">
                    <p class="text-xl text-[#ABBCC9]">no brand</p>
                </div>
            </div>
        </div>
        <DeleteConfirmation v-if="showDelConfirm" @close="closeDelConfirm" message="Do you want to delete this brand?"
            @delete="deleteBrand" />
    </div>
</template>

<style scoped></style>