[![](https://jitpack.io/v/luksiv/entdiffy.svg)](https://jitpack.io/#luksiv/entdiffy)


# Usage in your project
Firstly add jitpack.io Maven repository:
```
allprojects {
    repositories {
        maven { url 'https://jitpack.io' }
        ...
    }
}
```
Then add these dependancies:
```
implementation 'com.github.luksiv.entdiffy:annotations:latestVersion'
kapt 'com.github.luksiv.entdiffy:processor:latestVersion'
```

# Instructions
## Annotate an entity
To start using the library add the annotation `@DiffEntity` to your entity, for example:
```
@DiffEntity
data class Person(
    val name: String,
    val age: Int,
    val work: Work
)

data class Work(
    val position: String,
    val salary: Double
)
```
## Use the generated DiffUtil
The library generates a `DiffUtil` class (in this example `PersonDiffUtil`) which has 1 method named `calculateDiff`:
```
public object PersonDiffUtil {
    public fun calculateDiff(first: Person, second: Person): PersonDiffResult {
        return PersonDiffResult(
            nameChanged = first.name != second.name,
            ageChanged = first.age != second.age,
            workChanged = first.work != second.work
        )
    }
}
```
Or if you preffer using extensions, there are also those:
```
public object PersonDiffUtilExtensions {
    public fun Person.calculateDiff(other: Person?): PersonDiffResult = PersonDiffUtil.calculateDiff(this, other)
}
```
## Results 
The library also generates the results class, which has boolean variables whether a parameter has changed:
```
public data class PersonDiffResult(
    public val nameChanged: Boolean,
    public val ageChanged: Boolean,
    public val workChanged: Boolean
)
```
## Now joining everything together
Code:
```
fun main() {
    val a = Person("James", 20, Work("Android developer", 1000.0))
    val b = Person("James", 30, Work("Head of Mobile", 3000.00))

    println(a)
    println(b)

    println(PersonDiffUtil.calculateDiff(a, b))
}
```
Output:
```
Person(name=James, age=20, work=Work(position=Android developer, salary=1000.0))
Person(name=James, age=30, work=Work(position=Head of Mobile, salary=3000.0))
PersonDiffResult(nameChanged=false, ageChanged=true, workChanged=true)
```
