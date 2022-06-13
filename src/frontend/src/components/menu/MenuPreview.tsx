import React from "react";
import {Button, Row} from "@foreflight/ffui";
import {getChildrenById, getTopLevelMenuChoices} from "../../client";
import MenuChoice from "../../types/MenuChoice";

type MenuChoiceSelectable = MenuChoice &
    {selected: boolean}

type MenuPreviewState = {
    choices: MenuChoiceSelectable[][]
}

class MenuPreview extends React.Component<{}, MenuPreviewState>{

    constructor({}) {
        super({})
        this.state = {choices: []}
    }

    async componentDidMount() {

        let topLevelChoices: MenuChoiceSelectable[] = [];
        await getTopLevelMenuChoices().then(response => {
            return response.json() as Promise<any[]>;
        }).then(data =>{
            data.forEach((choice) =>{
                topLevelChoices.push(choice as MenuChoiceSelectable)
            })
        });

        this.setState((state) =>{
            let newChoices: MenuChoiceSelectable[][] = [];
            newChoices.push(topLevelChoices);
            newChoices[0].forEach((choice) => {
                choice.selected = false;
            })
            return {choices: newChoices}
        })

        console.log(this.state);
    }

    async buttonClick(row: number, choiceIdx: number){
        // Ensure that state is not changed if a choice is already selected
        if(this.state.choices[row][choiceIdx].selected){
            return;
        }

        // Get children of selected choice
        let choiceSelected = this.state.choices[row][choiceIdx];
        let children: MenuChoiceSelectable[] = [];
        await getChildrenById(choiceSelected.id).then(response =>{
            return response.json() as Promise<any[]>
        }).then(data => {
            data.forEach((child) => {
                children.push(child as MenuChoiceSelectable);
            })
        })
        children.forEach((child) => {
            child.selected = false;
        })


        this.setState((state) =>{
            // The clicked choice is marked as selected, others are deselected
            state.choices[row].forEach((choice, idx) => {
                choice.selected = choiceIdx == idx;
            })

            // Clear all rows after the selected one
            for(let r = row + 1; r < state.choices.length; r++){
                state.choices[r] = []
            }

            // Add new children of selected choice to state
            state.choices.push(children);

            return state;
        })
    }

    render(){
        return(
            <div>
                {this.state.choices.map((choiceRow, rowIdx) =>(
                    <Row key={rowIdx}>
                        {choiceRow.map((choice, choiceIdx) => (
                            <Button color={choice.selected ? "green" : "blue"} key={choice.id} onClick={() => {
                                this.buttonClick(rowIdx, choiceIdx)
                            }}>
                                {choice.name}
                            </Button>
                        ))}
                    </Row>
                ))}
            </div>
        );
    }
}

export default MenuPreview;