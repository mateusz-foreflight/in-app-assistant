import React from "react";
import MenuChoiceList from "../components/menuChoice/MenuChoiceList";
import MenuChoice from "../types/MenuChoice";
import MenuChoiceEditor from "../components/menuChoice/MenuChoiceEditor";
import Navbar from "../components/common/Navbar";
import {cache} from "../components/common/Cache";

type MenuModificationPageState = {
    selectedChoice: MenuChoice | null;
    searchVal: string;
    displayedChoices: MenuChoice[];
}

class MenuModificationPage extends React.Component<{}, MenuModificationPageState>{
    constructor(props: {}) {
        super(props);

        this.state = {
            selectedChoice: null,
            searchVal: "",
            displayedChoices: []
        }
    }

    updateChoices = async () => {
        await cache.updateMenuChoices();
        this.updateDisplayedChoices();
    }

    async updateResources() {
        await cache.updateResources();
    }

    updateDisplayedChoices = () => {
        let displayedChoices: MenuChoice[] = [];
        for(const choice of cache.getAllMenuChoices()){
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
        if(this.state.searchVal !== prevState.searchVal){
            this.updateDisplayedChoices();
        }
    }

    updateSelectedChoice = (newChoice: MenuChoice) => {
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