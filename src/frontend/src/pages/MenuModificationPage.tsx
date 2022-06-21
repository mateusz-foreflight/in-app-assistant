import React from "react";
import MenuChoiceList from "../components/menuChoiceList/MenuChoiceList";
import {getAllMenuChoices, getAllResources, getChildrenById, extract} from "../client";
import MenuChoice from "../types/MenuChoice";
import MenuChoiceWithChildren from "../types/MenuChoiceWithChildren";
import MenuChoiceEditor from "../components/menuChoiceEditor/MenuChoiceEditor";
import Resource from "../types/Resource";
import Navbar from "../components/navbar/Navbar";

type MenuModificationPageState = {
    choices: MenuChoiceWithChildren[];
    resources: Resource[];
    selectedChoice: MenuChoiceWithChildren | null;
    searchVal: string;
    displayedChoices: MenuChoiceWithChildren[];
}

class MenuModificationPage extends React.Component<{}, MenuModificationPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            choices: [],
            resources: [],
            selectedChoice: null,
            searchVal: "",
            displayedChoices: []
        }
    }

    async getChildren(choiceId: number) {
        return await extract<MenuChoice>(getChildrenById(choiceId))
    }

    updateChoices = async () => {
        let fetchedChoices = await extract<MenuChoiceWithChildren>(getAllMenuChoices());

        for (const choice of fetchedChoices) {
            choice.children = await this.getChildren(choice.id);
        }

        this.setState({
            choices: fetchedChoices
        })
    }

    async updateResources() {
        this.setState({
            resources: await extract<Resource>(getAllResources())
        })
    }

    updateDisplayedChoices = () => {
        let displayedChoices: MenuChoiceWithChildren[] = [];
        for(const choice of this.state.choices){
            if(choice.name.search(new RegExp(`${this.state.searchVal}`, "i")) >= 0){
                displayedChoices.push(choice);
            }
        }

        this.setState({
            displayedChoices: displayedChoices
        })
    }

    async componentDidMount() {
        await this.updateChoices();
        await this.updateResources();
    }

    componentDidUpdate(prevProps: Readonly<{}>, prevState: Readonly<MenuModificationPageState>) {
        if(this.state.searchVal !== prevState.searchVal || this.state.choices !== prevState.choices){
            this.updateDisplayedChoices();
        }
    }

    updateSelectedChoice = (newChoice: MenuChoiceWithChildren) => {
        this.setState({
            selectedChoice: newChoice
        });
    }

    deselectChoice = () => {
        this.setState({
            selectedChoice: null
        })
    }

    render() {
        return (
            <div>
                <Navbar activePage={1}/>

                <MenuChoiceEditor key={this.state.selectedChoice?.id}
                                  choiceBeingEdited={this.state.selectedChoice}
                                  allChoices={this.state.choices}
                                  allResources={this.state.resources}
                                  deactivateCallback={this.deselectChoice}
                                  saveCallback={this.updateChoices}>
                </MenuChoiceEditor>
                <MenuChoiceList choices={this.state.displayedChoices}
                                selectCallback={this.updateSelectedChoice}
                                searchCallback={searchVal => this.setState({searchVal: searchVal})}
                                selectedChoiceId={this.state.selectedChoice ? this.state.selectedChoice.id : null}>
                </MenuChoiceList>
            </div>
        )
    }
}

export default MenuModificationPage;