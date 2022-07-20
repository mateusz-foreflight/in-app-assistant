type MenuChoiceDTO = {
    name: string,
    parentId: number | null,
    resourceIds: number[],
    isPublic: boolean
}

export default MenuChoiceDTO;