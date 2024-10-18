import java.io.*;
import java.util.ArrayList;
import java.util.List;

// Паттерн Одиночка
class Logger {
    private static Logger instance;
    private LogLevel currentLogLevel = LogLevel.INFO;
    private String logFilePath = "logs.txt";

    private Logger() {
        // Приватный конструктор
    }

    public static synchronized Logger getInstance() {
        if (instance == null) {
            instance = new Logger();
        }
        return instance;
    }

    public void setLogLevel(LogLevel level) {
        this.currentLogLevel = level;
    }

    public void log(String message, LogLevel level) {
        if (level.ordinal() >= currentLogLevel.ordinal()) {
            try (BufferedWriter writer = new BufferedWriter(new FileWriter(logFilePath, true))) {
                writer.write(level + ": " + message);
                writer.newLine();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }
}

// Уровни логирования
enum LogLevel {
    INFO, WARNING, ERROR
}

// Чтение логов
class LogReader {
    public void readLogs(String logFilePath, LogLevel level) {
        try (BufferedReader reader = new BufferedReader(new FileReader(logFilePath))) {
            String line;
            while ((line = reader.readLine()) != null) {
                if (line.startsWith(level.toString())) {
                    System.out.println(line);
                }
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

// Паттерн Строитель
interface IReportBuilder {
    void setHeader(String header);
    void setContent(String content);
    void setFooter(String footer);
    void addSection(String sectionName, String sectionContent);
    void setStyle(ReportStyle style);
    Report getReport();
}

class TextReportBuilder implements IReportBuilder {
    private Report report = new Report();

    public void setHeader(String header) {
        report.setHeader(header);
    }

    public void setContent(String content) {
        report.setContent(content);
    }

    public void setFooter(String footer) {
        report.setFooter(footer);
    }

    public void addSection(String sectionName, String sectionContent) {
        report.addSection(sectionName, sectionContent);
    }

    public void setStyle(ReportStyle style) {
        report.setStyle(style);
    }

    public Report getReport() {
        return report;
    }
}

// Другие строители (HtmlReportBuilder и PdfReportBuilder) могут быть добавлены аналогично

class Report {
    private String header;
    private String content;
    private String footer;
    private List<Section> sections = new ArrayList<>();
    private ReportStyle style;

    public void setHeader(String header) {
        this.header = header;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public void setFooter(String footer) {
        this.footer = footer;
    }

    public void addSection(String sectionName, String sectionContent) {
        sections.add(new Section(sectionName, sectionContent));
    }

    public void setStyle(ReportStyle style) {
        this.style = style;
    }

    public void export() {
        // Экспорт отчета (пока просто вывод на экран)
        System.out.println("Header: " + header);
        System.out.println("Content: " + content);
        for (Section section : sections) {
            System.out.println("Section: " + section.name + " - " + section.content);
        }
        System.out.println("Footer: " + footer);
        System.out.println("Report Style: " + style);
    }
}

class Section {
    String name;
    String content;

    public Section(String name, String content) {
        this.name = name;
        this.content = content;
    }
}

class ReportStyle {
    private String backgroundColor;
    private String fontColor;
    private int fontSize;

    public ReportStyle(String backgroundColor, String fontColor, int fontSize) {
        this.backgroundColor = backgroundColor;
        this.fontColor = fontColor;
        this.fontSize = fontSize;
    }

    @Override
    public String toString() {
        return "Background Color: " + backgroundColor + ", Font Color: " + fontColor + ", Font Size: " + fontSize;
    }
}

// Паттерн Прототип
interface ICloneable {
    ICloneable clone();
}

class Character implements ICloneable {
    private String name;
    private int health;
    private int strength;
    private int agility;
    private int intelligence;
    private Weapon weapon;
    private Armor armor;

    public Character(String name, int health, int strength, int agility, int intelligence, Weapon weapon, Armor armor) {
        this.name = name;
        this.health = health;
        this.strength = strength;
        this.agility = agility;
        this.intelligence = intelligence;
        this.weapon = weapon;
        this.armor = armor;
    }

    public ICloneable clone() {
        return new Character(name, health, strength, agility, intelligence, (Weapon) weapon.clone(), (Armor) armor.clone());
    }
}

class Weapon implements ICloneable {
    private String name;
    private int damage;

    public Weapon(String name, int damage) {
        this.name = name;
        this.damage = damage;
    }

    public ICloneable clone() {
        return new Weapon(name, damage);
    }
}

class Armor implements ICloneable {
    private String name;
    private int defense;

    public Armor(String name, int defense) {
        this.name = name;
        this.defense = defense;
    }

    public ICloneable clone() {
        return new Armor(name, defense);
    }
}

// Тестирование
public class DesignPatternsDemo {
    public static void main(String[] args) {
        // Тестирование паттерна Одиночка
        Logger logger = Logger.getInstance();
        logger.log("Приложение запущено", LogLevel.INFO);
        logger.log("Произошло предупреждение", LogLevel.WARNING);
        logger.log("Произошла ошибка", LogLevel.ERROR);

// Чтение логов
        LogReader logReader = new LogReader();
        logReader.readLogs("logs.txt", LogLevel.ERROR);

// Тестирование паттерна Строитель
        ReportStyle style = new ReportStyle("белый", "черный", 12);
        TextReportBuilder reportBuilder = new TextReportBuilder();
        reportBuilder.setHeader("Ежемесячный отчет");
        reportBuilder.setContent("Это основное содержание отчета.");
        reportBuilder.addSection("Раздел 1", "Содержание для раздела 1");
        reportBuilder.setFooter("Конец отчета");
        reportBuilder.setStyle(style);

        Report report = reportBuilder.getReport();
        report.export();

// Тестирование паттерна Прототип
        Weapon sword = new Weapon("Меч", 50);
        Armor shield = new Armor("Щит", 30);
        Character knight = new Character("Рыцарь", 100, 20, 15, 10, sword, shield);

        Character clonedKnight = (Character) knight.clone();
        System.out.println("Создан клон персонажа с именем: " + clonedKnight);

    }
}
