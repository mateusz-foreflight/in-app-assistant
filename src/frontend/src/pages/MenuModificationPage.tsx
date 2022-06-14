import React from "react";
import MenuChoiceList from "../components/menuChoiceList/MenuChoiceList";
import {getAllMenuChoices, getChildrenById, updateMenuChoice} from "../client";
import MenuChoice from "../types/MenuChoice";
import MenuChoiceWithChildren from "../types/MenuChoiceWithChildren";
import MenuChoiceEditor from "../components/menuChoiceEditor/MenuChoiceEditor";

type MenuModificationPageState = {
    choices: MenuChoiceWithChildren[];
    selectedChoice: MenuChoiceWithChildren | null;
}

class MenuModificationPage extends React.Component<{}, MenuModificationPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            choices: [],
            selectedChoice: null
        }
    }

    async getChildren(choiceId: number) {
        let children: MenuChoice[] = [];

        await getChildrenById(choiceId).then(response =>{
            return response.json() as Promise<any[]>
        }).then(data => {
            data.forEach((child) => {
                children.push(child as MenuChoice);
            })
        })

        return children;
    }

    updateChoices = async () => {
        let fetchedChoices: MenuChoiceWithChildren[] = [];

        await getAllMenuChoices().then(response => {
            return response.json() as Promise<any[]>;
        }).then(data => {
            data.forEach((choice) => {
                fetchedChoices.push(choice as MenuChoiceWithChildren)
            })
        });

        for (const choice of fetchedChoices) {
            choice.children = await this.getChildren(choice.id);
        }

        this.setState({
            choices: fetchedChoices
        })
    }

    async componentDidMount() {
        await this.updateChoices();
    }

    updateSelectedChoice = (newChoice: MenuChoiceWithChildren) => {
        this.setState({
            selectedChoice: newChoice
        });
    }

    cancelButton = () => {
        this.setState({
            selectedChoice: null
        })
    }

    render() {
        return (
            <div>
                Menu Modification:
                <MenuChoiceEditor editingChoice={this.state.selectedChoice}
                                  cancelCallback={this.cancelButton}
                                  saveCallback={this.updateChoices}>
                </MenuChoiceEditor>
                <MenuChoiceList choices={this.state.choices}
                                selectCallback={this.updateSelectedChoice}
                                selectedChoiceId={this.state.selectedChoice ? this.state.selectedChoice.id : null}>
                </MenuChoiceList>
            </div>
        )
    }
}

export default MenuModificationPage;