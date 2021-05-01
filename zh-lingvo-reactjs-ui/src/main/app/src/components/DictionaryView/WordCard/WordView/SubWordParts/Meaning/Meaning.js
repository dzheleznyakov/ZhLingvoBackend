import React from 'react';
import { useSelector } from 'react-redux';
import PropTypes from 'prop-types';

import classes from './Meaning.module.scss';

import { EditableRemark, Remark } from '..';
import { EditableTranslation, Translation, NULL_TRANSLATION } from '..';
import { EditableExample, Example, NULL_EXAMPLE } from '..';
import { meaningType } from '../../wordTypes';
import * as selectors from '../../../../../../store/selectors';

const TRANSLATIONS_PATH_SEGMENT = 'translations';
const EXAMPLES_PATH_SEGMENT = 'examples';

const sortById = (a, b) => {
    if (a === null)
        return 1;
    if (b === null)
        return -1;
    return a.id < b.id ? -1 : 1;
};

const getTranslations = (meaning, path, isEditing, editable) => {
    const TranslationComp = editable ? EditableTranslation : Translation;
    const translations = (meaning.translations || [])
        .sort(sortById)
        .map((tr, i) => (tr === null ? null :
            <TranslationComp 
                key={tr.id} 
                path={[...path, TRANSLATIONS_PATH_SEGMENT, `${i}`]} 
                entry={tr}
                postfix={i < meaning.translations.length - 1 ? '; ': ' '}
            />
        ));
    if (editable && isEditing)
        translations.push(<EditableTranslation
            key="translation-new"
            path={[...path, TRANSLATIONS_PATH_SEGMENT, `${meaning.translations.length}`]}
            entry={NULL_TRANSLATION}
        />);
    return translations;
};

const getExamples = (meaning, path, isEditing, editable) => {
    const ExampleComp = editable ? EditableExample : Example;
    const examples = (meaning.examples || [])
        .sort(sortById)
        .map((ex, i) => (ex === null ? null :
            <ExampleComp 
                key={ex.id} 
                path={[...path, EXAMPLES_PATH_SEGMENT, `${i}`]} 
                entry={ex}
            />
        ));
    if (editable && isEditing)
        examples.push(<EditableExample
            key="example-new"
            path={[...path, EXAMPLES_PATH_SEGMENT, `${meaning.translations.length}`]}
            entry={NULL_EXAMPLE}
        />);
    return examples;
};

const Meaning = props => {
    const { meaning, path, editable } = props;
    const isEditing = useSelector(selectors.isEditingSelector);

    const remarkPath = [...path, 'remark'];
    const RemarkComp = editable ? EditableRemark : Remark;
    const remark = <RemarkComp value={meaning.remark} path={remarkPath} />;
    const translations = getTranslations(meaning, path, isEditing, editable);
    const examples = getExamples(meaning, path, isEditing, editable);

    return (
        <div className={classes.Meaning}>
            {remark}
            {translations}
            {examples}
        </div>
    );
};

Meaning.propTypes = {
    meaning: meaningType.isRequired,
    path: PropTypes.arrayOf(PropTypes.string).isRequired,
    editable: PropTypes.bool,
};

Meaning.defaultProps = {
    editable: true,
}

export default Meaning;
