import React from "react";
import {Button, Heading} from "@foreflight/ffui";
import SelectableList from "../common/SelectableList";
import Source from "../../types/Source";

type SourceListProps = {
    sources: Source[];
    selectCallback: (source: Source) => void;
    searchCallback: (searchVal: string) => void;
    selectedSourceId: number | null;
}

class SourceList extends React.Component<SourceListProps, {}>{

    render() {
        return (
            <>
                <Heading>
                    Source List:
                </Heading>

                <SelectableList<Source>
                    columnNames={["Source Name", "Link"]}
                    items={this.props.sources}
                    getIdFunc={source => source.id}
                    selectedItemId={this.props.selectedSourceId}
                    selectCallback={this.props.selectCallback}
                    searchCallback={this.props.searchCallback}
                    columnFuncs={[
                        source => source.name,
                        source => {return {raw:
                                <Button
                                    small
                                    onClick={() => window.open(source.link)}
                                >
                                    Go To
                                </Button>}},
                    ]}
                />
            </>
        );
    }
}

export default SourceList;