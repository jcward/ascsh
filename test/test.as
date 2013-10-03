package
{
  import flash.display.Sprite;

  public class test extends Sprite
  {
    public function test():void
    {
      var literal:String = "He said,\nit's a frickin\' \"laser\"!";
      var val:String = CONFIG::V1;
      if (val != literal) {
        trace(literal);
        trace(val);
        throw new Error("Config param mismatch!");
      }
      trace("Success");
    }
  }
}
