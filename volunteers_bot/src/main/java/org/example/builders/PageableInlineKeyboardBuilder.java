package org.example.builders;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import org.example.enums.PageMoveEnum;
import org.example.pojo.dto.ButtonDto;
import org.telegram.telegrambots.meta.api.objects.replykeyboard.buttons.InlineKeyboardButton;

import java.util.Arrays;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
public class PageableInlineKeyboardBuilder extends InlineKeyboardBuilder {
    private static final Integer MAX_ROW_COUNT_IN_PAGE = 7;

    public static PageableInlineKeyboardBuilder create() {
        return new PageableInlineKeyboardBuilder();
    }

    public static int getPageCount(List<ButtonDto> buttonDtoList) {
        int rowButtonCount = buttonDtoList.size();
        int pageCount = rowButtonCount / MAX_ROW_COUNT_IN_PAGE;
        if (rowButtonCount % MAX_ROW_COUNT_IN_PAGE > 0) {
            pageCount++;
        }

        return pageCount;
    } // TODO : неверно, если несколько кнопок на одном ряду

    public void addButtonList(List<ButtonDto> buttonDtoList, int pageNumber) {
        for (ButtonDto buttonDto : buttonDtoList) {
            if (isCorrectRowForCurrentPage(pageNumber, buttonDto.getRow())) {
                addButton(buttonDto);
            }
        }

        if (buttonDtoList.size() > MAX_ROW_COUNT_IN_PAGE) {
            addMoveButtons();
        }
    }

    @Override
    protected void addButton(ButtonDto button) {
        int row = button.getRow() % MAX_ROW_COUNT_IN_PAGE;
        addButton(row, button);
    }

    public void addMoveButtons() {
        rowButtonList.add(getPageMoveButtonList());
    }

    private List<InlineKeyboardButton> getPageMoveButtonList() {
        return Arrays.asList(
                (new ButtonDto(PageMoveEnum.PREV.name(), PageMoveEnum.PREV.getDescription())).toKeyboardButton(),
                (new ButtonDto(PageMoveEnum.NEXT.name(), PageMoveEnum.NEXT.getDescription())).toKeyboardButton()
        );
    }

    private boolean isCorrectRowForCurrentPage(int pageNumber, int row) {
        int minRow = pageNumber * MAX_ROW_COUNT_IN_PAGE;
        int maxRow = minRow + MAX_ROW_COUNT_IN_PAGE - 1;
        return minRow <= row && row <= maxRow;
    }
}
